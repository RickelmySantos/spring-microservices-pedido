import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, tap, throwError } from 'rxjs';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { PedidoService } from 'src/app/services/pedido.service';

@Injectable({ providedIn: 'root' })
export class CarrinhoService {
    private readonly pedidoService: PedidoService = inject(PedidoService);

    private readonly itensSubject = new BehaviorSubject<ItemCarrinho[]>([]);

    public readonly itensCarrinho$: Observable<ItemCarrinho[]> = this.itensSubject.asObservable();

    public readonly totalCarrinho$: Observable<number> = this.itensCarrinho$.pipe(map(itens => itens.reduce((total, item) => total + item.preco * item.quantidade, 0)));

    public readonly quantidadeItens$: Observable<number> = this.itensCarrinho$.pipe(map(itens => itens.length));

    adicionarItem(item: MenuCardapio): void {
        const itensAtuais = [...this.itensSubject.value];
        const itemExistente = itensAtuais.find(i => i.id === item.id);

        if (itemExistente) {
            itemExistente.quantidade++;
        } else {
            itensAtuais.push({ ...item, quantidade: 1 });
        }

        this.itensSubject.next(itensAtuais);
    }

    alterarQuantidade(itemId: number, delta: number): void {
        let itensAtuais = [...this.itensSubject.value];
        const item = itensAtuais.find(i => i.id === itemId);

        if (item) {
            item.quantidade += delta;

            if (item.quantidade <= 0) {
                itensAtuais = itensAtuais.filter(i => i.id !== itemId);
            }
        }

        this.itensSubject.next(itensAtuais);
    }

    limparCarrinho(): void {
        this.itensSubject.next([]);
    }

    finalizarPedido(): Observable<any> {
        const itensAtuais = this.itensSubject.value;

        if (!itensAtuais.length) {
            return throwError(() => new Error('Seu carrinho está vazio... 😔'));
        }

        const pedidoValido = itensAtuais.every(item => item.quantidade && item.quantidade > 0);
        if (!pedidoValido) {
            return throwError(() => new Error('Alguns itens possuem quantidade inválida...'));
        }

        const pedidoPayload = {
            descricao: 'Pedido de múltiplos itens',
            itens: itensAtuais.map(i => ({
                produtoId: i.id,
                quantidade: i.quantidade,
            })),
        };

        console.log('Enviando payload:', pedidoPayload);

        return this.pedidoService.registrarPedido(pedidoPayload).pipe(
            tap(() => {
                console.log('Pedido registrado com sucesso, limpando o carrinho.');
                this.limparCarrinho();
            }),
            catchError(err => {
                console.error('Houve um problema ao registrar o pedido.', err);
                return throwError(() => new Error('Houve um problema ao finalizar seu pedido... 😔'));
            })
        );
    }
    atualizarCarrinho(itens: ItemCarrinho[]) {
        this.itensSubject.next(itens);
    }
}

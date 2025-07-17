import { inject, Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { CriarPedidoRequest } from '../models/criarPedido.model';
import { Pedido } from '../models/pedido.model';
import { CarrinhoService } from './carrinho.service';
import { PedidoService } from './pedido.service';

@Injectable({ providedIn: 'root' })
export class CheckoutService {
    private readonly carrinhoService: CarrinhoService = inject(CarrinhoService);
    private readonly pedidoService: PedidoService = inject(PedidoService);

    finalizarPedido(): Observable<Pedido> {
        const itens = this.carrinhoService.carrinhoItens;

        if (!itens.length) {
            return throwError(() => new Error('Seu carrinho está vazio... 😔'));
        }
        const pedidoValido = itens.every(i => i.quantidade > 0);
        if (!pedidoValido) {
            return throwError(() => new Error('Todos os itens devem ter quantidade maior que zero.'));
        }

        const payload: CriarPedidoRequest = {
            observacao: 'Pedido realizado via checkout',
            itensPedido: itens.map(item => ({
                produtoId: item.id,
                quantidade: item.quantidade,
                precoUnitario: item.preco,
                nomeProduto: item.nome,
            })),
        };
        return this.pedidoService.registrarPedido(payload).pipe(
            tap(() => this.carrinhoService.limparCarrinho()),
            catchError(err => {
                console.error('Erro ao finalizar pedido:', err);
                return throwError(() => new Error('Erro ao finalizar pedido. Tente novamente.'));
            })
        );
    }
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { Produto } from '../models/produto.model';

@Injectable({ providedIn: 'root' })
export class CarrinhoService {
    private readonly itensSubject = new BehaviorSubject<ItemCarrinho[]>([]);

    public readonly itensCarrinho$: Observable<ItemCarrinho[]> = this.itensSubject.asObservable();

    public readonly totalCarrinho$: Observable<number> = this.itensCarrinho$.pipe(map(itens => itens.reduce((total, item) => total + item.preco * item.quantidade, 0)));

    public readonly quantidadeItens$: Observable<number> = this.itensCarrinho$.pipe(map(itens => itens.length));

    adicionarItem(item: Produto): void {
        const itensAtuais = [...this.carrinhoItens];
        const itemExistente = itensAtuais.find(i => i.id === item.id);

        if (itemExistente) {
            itemExistente.quantidade++;
        } else {
            itensAtuais.push({ ...item, quantidade: 1 });
        }

        this.itensSubject.next(itensAtuais);
    }

    incrementarQuantidadeOuRemover(itemId: number, delta: number): void {
        let itensAtuais = [...this.carrinhoItens];
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

    atualizarCarrinho(itens: ItemCarrinho[]) {
        this.itensSubject.next(itens);
    }

    get carrinhoItens(): ItemCarrinho[] {
        return this.itensSubject.value;
    }
}

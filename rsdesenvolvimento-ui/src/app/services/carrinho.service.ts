import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';

@Injectable({ providedIn: 'root' })
export class CarrinhoService {
    private itens = new BehaviorSubject<ItemCarrinho[]>([]);

    public itensCarrinho$ = this.itens.asObservable();

    adicionarItem(item: MenuCardapio) {
        const itensAtuais = this.itens.value;
        const existente = itensAtuais.find(i => i.id === item.id);

        if (existente) {
            existente.quantidade += 1;
        } else {
            itensAtuais.push({ ...item, quantidade: 1 });
        }

        this.itens.next([...itensAtuais]);
    }
    atualizarCarrinho(itens: ItemCarrinho[]) {
        this.itens.next(itens);
    }
    limparCarrinho() {
        this.itens.next([]);
    }

    getItens(): MenuCardapio[] {
        return this.itens.value;
    }
}

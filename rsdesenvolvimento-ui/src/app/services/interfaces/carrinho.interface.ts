import { Observable } from 'rxjs';
import { ItemCarrinho } from '../../models/itemCarrinho.model';

export interface ICarrinhoService {
    readonly itensCarrinho$: Observable<ItemCarrinho[]>;

    limparCarrinho(): void;

    get carrinhoItens(): ItemCarrinho[];
}

import { Produto } from './produto.model';

export interface ItemCarrinho extends Produto {
    quantidade: number;
}

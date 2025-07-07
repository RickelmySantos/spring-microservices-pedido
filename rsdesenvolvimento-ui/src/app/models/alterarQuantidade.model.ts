import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';

export interface AlterarQuantidadeEvent {
    item: ItemCarrinho;
    delta: number;
}

import { MenuCardapio } from 'src/app/models/menu-cardapio.model';

export interface ItemCarrinho extends MenuCardapio {
    quantidade: number;
}

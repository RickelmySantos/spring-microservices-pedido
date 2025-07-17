import { Entity } from 'src/app/core/components/models/entity.model';
import { ItemPedido } from 'src/app/models/itemPedido.model';
import { StatusEnum } from 'src/app/shared/enums/statusEnum.enum';

export interface Pedido extends Entity<number> {
    usuarioId: string;
    observacao?: string;
    nomeUsuario: string;
    emailUsuario: string;
    status: StatusEnum;
    itensPedido: ItemPedido[];
}

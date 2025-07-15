import { Entity } from 'src/app/core/components/models/entity.model';

export interface ItemPedido extends Entity<number> {
    produtoId: number;
    nomeProduto: string;
    quantidade: number;
    precoUnitario: number;
}

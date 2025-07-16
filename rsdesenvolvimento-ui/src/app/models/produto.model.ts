import { Entity } from 'src/app/core/components/models/entity.model';

export interface Produto extends Entity<number> {
    nome: string;
    descricao: string;
    preco: number;
    categoria: string;
    estoque: number;
    imagemUrl: string;
    alt: string;
}

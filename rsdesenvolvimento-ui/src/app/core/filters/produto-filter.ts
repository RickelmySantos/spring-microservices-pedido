import { Produto } from '../../models/produto.model';

export interface IProdutoFilter {
    filtrar(produtos: Produto[], categoria: string): Produto[];
}

export class ProdutoCategoriaFilter implements IProdutoFilter {
    public filtrar(produtos: Produto[], categoria: string): Produto[] {
        if (categoria === 'all') {
            return produtos;
        }
        return produtos.filter(produto => produto.categoria === categoria);
    }
}

import { Observable } from 'rxjs';
import { Produto } from '../../models/produto.model';

export interface IProdutoRepository {
    listarProdutos(): Observable<Produto[]>;
}

export interface ICardapioState {
    readonly categoriaSelecionada$: Observable<string>;
    readonly produtosFiltrados$: Observable<Produto[]>;
    selecionarCategoria(categoria: string): void;
    resetarCategoria(): void;
}

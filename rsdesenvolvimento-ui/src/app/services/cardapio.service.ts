import { Inject, Injectable, InjectionToken, OnDestroy } from '@angular/core';
import { combineLatest, map, Observable, shareReplay } from 'rxjs';
import { IProdutoFilter, ProdutoCategoriaFilter } from '../core/filters/produto-filter';

import { CategoriaStateManager } from '../core/state/categoria-state-manager';
import { Produto } from '../models/produto.model';
import { ICardapioState, IProdutoRepository } from './interfaces/cardapio.interfaces';

export const PRODUTO_REPOSITORY_TOKEN = new InjectionToken<IProdutoRepository>('IProdutoRepository');

@Injectable({ providedIn: 'root' })
export class CardapioState implements ICardapioState, OnDestroy {
    private readonly categoriaState: CategoriaStateManager;
    private readonly produtoFilter: IProdutoFilter;
    private readonly produtos$: Observable<Produto[]>;

    public readonly categoriaSelecionada$: Observable<string>;
    public readonly produtosFiltrados$: Observable<Produto[]>;

    constructor(@Inject(PRODUTO_REPOSITORY_TOKEN) private readonly produtoRepository: IProdutoRepository) {
        this.categoriaState = new CategoriaStateManager();
        this.produtoFilter = new ProdutoCategoriaFilter();

        this.categoriaSelecionada$ = this.categoriaState.value$;
        this.produtos$ = this.produtoRepository.listarProdutos().pipe(shareReplay({ bufferSize: 1, refCount: true }));

        this.produtosFiltrados$ = combineLatest([this.categoriaSelecionada$, this.produtos$]).pipe(
            map(([categoria, produtos]) => this.produtoFilter.filtrar(produtos, categoria)),
            shareReplay({ bufferSize: 1, refCount: true })
        );
    }

    public selecionarCategoria(categoria: string): void {
        this.categoriaState.selecionarCategoria(categoria);
    }

    public resetarCategoria(): void {
        this.categoriaState.resetarCategoria();
    }

    public get categoriaAtual(): string {
        return this.categoriaState.currentValue;
    }

    public get isTodasCategorias(): boolean {
        return this.categoriaState.isTodasCategorias;
    }

    public ngOnDestroy(): void {
        this.categoriaState.destroy();
    }
}

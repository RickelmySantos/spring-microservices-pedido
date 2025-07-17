import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable, shareReplay } from 'rxjs';
import { EstoqueService } from 'src/app/services/estoque.service';
import { Produto } from '../models/produto.model';

@Injectable({ providedIn: 'root' })
export class CardapioState {
    private readonly categoriaSelecionadaSubject = new BehaviorSubject<string>('all');
    public readonly categoriaSelecionada$ = this.categoriaSelecionadaSubject.asObservable();
    private readonly produtos$: Observable<Produto[]>;

    public readonly produtosFiltrados$: Observable<Produto[]>;

    constructor(private readonly estoqueService: EstoqueService) {
        this.produtos$ = this.estoqueService.listarProdutos().pipe(shareReplay({ bufferSize: 1, refCount: true }));

        this.produtosFiltrados$ = combineLatest([this.categoriaSelecionada$, this.produtos$]).pipe(
            map(([categoria, produtos]) => {
                if (categoria === 'all') {
                    return produtos;
                }
                return produtos.filter(produto => produto.categoria === categoria);
            })
        );
    }

    public selecionarCategoria(categoria: string): void {
        this.categoriaSelecionadaSubject.next(categoria);
    }
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable, shareReplay } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { EstoqueService } from 'src/app/services/estoque.service';

@Injectable({ providedIn: 'root' })
export class CardapioState {
    private readonly categoriaSelecionadaSubject = new BehaviorSubject<string>('all');
    public readonly categoriaSelecionada$ = this.categoriaSelecionadaSubject.asObservable();
    private readonly produtos$: Observable<MenuCardapio[]>;

    public readonly produtosFiltrados$: Observable<MenuCardapio[]>;

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

import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, switchMap } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { EstoqueService } from 'src/app/services/estoque.service';

@Injectable({ providedIn: 'root' })
export class CardapioService {
    private categoriaSelecionada = new BehaviorSubject<string>('all');

    public categoriaSelecionada$: Observable<string> = this.categoriaSelecionada.asObservable();
    public produtosFiltrados$: Observable<MenuCardapio[]>;

    constructor(private estoqueService: EstoqueService) {
        this.produtosFiltrados$ = this.categoriaSelecionada.pipe(
            switchMap(categoria =>
                this.estoqueService.listarProdutos().pipe(
                    map(produtos => {
                        if (categoria === 'all') {
                            return produtos;
                        }
                        return produtos.filter(item => item.categoria === categoria);
                    })
                )
            )
        );
    }

    /**
     * Atualiza a categoria selecionada para filtrar os produtos.
     */
    public selecionarCategoria(categoria: string): void {
        this.categoriaSelecionada.next(categoria);
    }
}

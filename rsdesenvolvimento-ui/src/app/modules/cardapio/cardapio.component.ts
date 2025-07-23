import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { map, Observable } from 'rxjs';
import { CardapioState } from 'src/app/services/cardapio.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { Produto } from '../../models/produto.model';
import { CarrinhoService } from '../../shared/components/carrinho/service/carrinho.service';
import { CardapioCategoriaComponent } from './categoria/cardapio-categoria.component';
import { CardapioCardComponent } from './menu/cardapio-menu.component';

@Component({
    selector: 'app-cardapio',
    template: `
        <section id="cardapio" class="cardapio">
            <div class="container">
                <div class="cardapio__header">
                    <h2>Nosso Cardápio</h2>
                    <p>Descubra as ricas tradições culinárias da Amazônia através de nosso menu cuidadosamente elaborado.</p>
                </div>

                <app-cardapio-categoria [categoriaAtiva]="cardapioState.categoriaSelecionada$ | async" (onCategoriaChange)="onCategoriaChange($event)"></app-cardapio-categoria>

                <div class="cardapio__grid">
                    <ng-container *ngIf="produtos$ | async as produtos">
                        <app-cardapio-card *ngFor="let item of produtos; trackBy: trackByProdutoId" [item]="item" (adiconarProduto)="adcionarProduto($event)"></app-cardapio-card>
                    </ng-container>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor, NgIf, CardapioCategoriaComponent, CardapioCardComponent],
})
export class CardapioComponent {
    private readonly cloudinaryBaseUrl = 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/';

    produtos$: Observable<Produto[]>;

    constructor(protected cardapioState: CardapioState, protected carrinhoService: CarrinhoService) {
        this.produtos$ = this.cardapioState.produtosFiltrados$.pipe(
            map(produtos =>
                produtos.map(produto => ({
                    ...produto,
                    imagemUrl: produto.imagemUrl.replace(this.cloudinaryBaseUrl, ''),
                }))
            )
        );
    }

    onCategoriaChange(categoria: string): void {
        this.cardapioState.selecionarCategoria(categoria);
    }

    adcionarProduto(item: Produto): void {
        this.carrinhoService.adcionarProduto(item);
    }
    trackByProdutoId(index: number, item: Produto): string | number {
        return item.id;
    }
}

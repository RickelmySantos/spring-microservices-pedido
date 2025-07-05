import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { CardapioState } from 'src/app/services/cardapio.service';
import { CarrinhoService } from 'src/app/services/carrinho.service';
import { SharedModule } from 'src/app/shared/shared.module';
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
                    <ng-container *ngIf="cardapioState.produtosFiltrados$ | async as produtos">
                        <app-cardapio-card *ngFor="let item of produtos; trackBy: trackByProdutoId" [item]="item" (adiconarProduto)="realizarPedido($event)"></app-cardapio-card>
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
    constructor(protected cardapioState: CardapioState, protected carrinhoService: CarrinhoService) {}

    onCategoriaChange(categoria: string): void {
        this.cardapioState.selecionarCategoria(categoria);
    }

    realizarPedido(item: MenuCardapio): void {
        this.carrinhoService.adicionarItem(item);
    }
    trackByProdutoId(index: number, item: MenuCardapio): string | number {
        return item.id;
    }
}

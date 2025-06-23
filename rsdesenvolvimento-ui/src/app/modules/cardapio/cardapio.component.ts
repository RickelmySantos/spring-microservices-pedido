import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { EstoqueService } from 'src/app/services/estoque.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { CardapioCategoriaComponent } from './categoria/cardapio-categoria.component';
import { CardapioMenuComponent } from './menu/cardapio-menu.component';

@Component({
    selector: 'app-cardapio',
    template: `
        <section id="menu" class="menu">
            <div class="container">
                <div class="section-title">
                    <h2>Nosso Cardápio</h2>
                    <p>Descubra as ricas tradições culinárias do Brasil por meio do nosso menu cuidadosamente elaborado com pratos de todas as regiões do país.</p>
                </div>

                <div class="menu-categories">
                    <app-cardapio-categoria [categoriaAtiva]="categoriaSelecionada$ | async" (onCategoriaChange)="onCategoriaChange($event)"></app-cardapio-categoria>
                </div>

                <div class="menu-items">
                    <ng-container *ngFor="let item of produtosFiltrados$ | async">
                        <app-cardapio-menu [item]="item"></app-cardapio-menu>
                    </ng-container>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CardapioMenuComponent, NgFor, CardapioCategoriaComponent],
})
export class CardapioComponent implements OnInit {
    categoriaSelecionada$ = new BehaviorSubject<string>('all');

    produtosFiltrados$: Observable<MenuCardapio[]>;
    produtos$: Observable<MenuCardapio[]>;

    constructor(private estoqueService: EstoqueService) {}

    ngOnInit(): void {
        const todosProdutos$ = this.estoqueService.listarProdutos();
        this.produtosFiltrados$ = combineLatest([todosProdutos$, this.categoriaSelecionada$]).pipe(
            map(([produtos, categoria]) => {
                if (categoria === 'all') {
                    return produtos;
                }
                return produtos.filter(produto => produto.categoria === categoria);
            })
        );
    }

    onCategoriaChange(categoria: string): void {
        this.categoriaSelecionada$.next(categoria);
    }
}

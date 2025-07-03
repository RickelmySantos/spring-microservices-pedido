import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, map, Observable } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { EstoqueService } from 'src/app/services/estoque.service';
import { PedidoService } from 'src/app/services/pedido.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { CardapioCategoriaComponent } from './categoria/cardapio-categoria.component';
import { CardapioMenuComponent } from './menu/cardapio-menu.component';

@Component({
    selector: 'app-cardapio',
    template: `
        <section id="cardapio" class="cardapio">
            <div class="container">
                <div class="cardapio__header">
                    <h2>Nosso Cardápio</h2>
                    <p>Descubra as ricas tradições culinárias da Amazônia através de nosso menu cuidadosamente elaborado.</p>
                </div>

                <app-cardapio-categoria [categoriaAtiva]="categoriaSelecionada$ | async" (onCategoriaChange)="onCategoriaChange($event)"></app-cardapio-categoria>

                <div class="cardapio-menu-list ">
                    <ng-container *ngIf="produtosFiltrados$ | async as produtos">
                        <ng-container *ngFor="let item of produtos">
                            <app-cardapio-menu [item]="item" (adiconarProduto)="realizarPedido($event)"></app-cardapio-menu>
                        </ng-container>
                    </ng-container>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CardapioMenuComponent, NgFor, NgIf, CardapioCategoriaComponent],
})
export class CardapioComponent implements OnInit {
    categoriaSelecionada$ = new BehaviorSubject<string>('all');

    produtosFiltrados$: Observable<MenuCardapio[]>;
    produtos$: Observable<MenuCardapio[]>;

    constructor(private estoqueService: EstoqueService, private pedidoService: PedidoService) {}

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

    realizarPedido(item: MenuCardapio) {
        this.pedidoService.criarPedido(item).subscribe({
            next: () => {
                console.log('Pedido realizado com sucesso!');
            },
            error: error => {
                console.error('Erro ao realizar o pedido:', error);
            },
        });
    }
}

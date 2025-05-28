import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { EstoqueService } from 'src/app/services/Estoque.service';
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
                    <app-cardapio-categoria></app-cardapio-categoria>
                </div>

                <div class="menu-items">
                    <ng-container *ngFor="let item of produtos$ | async">
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
    produtos$: Observable<MenuCardapio[]>;

    constructor(private estoqueService: EstoqueService) {}

    ngOnInit(): void {
        this.produtos$ = this.estoqueService.listarProdutos();
    }
}

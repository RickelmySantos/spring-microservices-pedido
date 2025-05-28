import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
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
                    <ng-container *ngFor="let item of items">
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
export class CardapioComponent {
    items: MenuCardapio[] = [
        {
            categoria: 'entradas',
            imagem: 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
            alt: 'Pão de Queijo',
            titulo: 'Pão de Queijo',
            preco: '$6.99',
            descricao: 'Traditional Brazilian cheese bread made with tapioca flour and Minas cheese, crispy on the outside and chewy on the inside.',
        },
        {
            categoria: 'entradas',
            imagem: 'https://images.unsplash.com/photo-1511690651692-7e258df116a2?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
            alt: 'Coxinha',
            titulo: 'Coxinha',
            preco: '$7.50',
            descricao: 'Teardrop-shaped croquettes filled with shredded chicken, cream cheese, and spices, coated in crispy breadcrumbs.',
        },
    ];
}

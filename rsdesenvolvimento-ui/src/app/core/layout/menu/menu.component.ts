import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faCartShopping } from '@fortawesome/free-solid-svg-icons';
import { MenuItem } from 'primeng/api';
import { SharedModule } from 'src/app/shared/shared.module';

import { CarrinhoComponent } from '../../../shared/components/carrinho/carrinho.component';
import { MenuItemComponent } from '../menu-item/menu-item.component';

@Component({
    selector: 'app-menu',
    template: `
        <section class="menu-bar container">
            <div class="menu-bar__logo">
                <i class="fas fa-utensils text-2xl"></i>

                <a href="#" class="text-2xl font-bold">
                    Sabores
                    <span>Brasil</span>
                </a>
            </div>

            <nav class="menu-bar__nav">
                <ul>
                    <ng-container *ngFor="let item of menu">
                        <app-menu-item [item]="item"></app-menu-item>
                    </ng-container>
                </ul>
            </nav>
            <div>
                <a (click)="modal.open()" class="cursor-pointer">
                    <fa-icon [icon]="['fas', 'cart-shopping']" class="text-2xl text-white"></fa-icon>
                </a>
                <app-carrinho [carrinhoItem]="items" #modal></app-carrinho>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor, MenuItemComponent, CarrinhoComponent],
})
export class MenuComponent {
    isCarrinhoVisible = false;

    constructor(library: FaIconLibrary) {
        library.addIcons(faCartShopping);
    }

    menu: MenuItem[] = [
        { label: 'Início', url: '#home' },
        { label: 'Cardápio', routerLink: '/menu' },
        { label: 'Sobre', url: '#about' },
        { label: 'Contato', url: '#contact' },
    ];

    items = [
        { name: 'Feijoada', quantity: 1 },
        { name: 'Pão de Queijo', quantity: 3 },
    ];

    openCarrinho(): void {
        this.isCarrinhoVisible = !this.isCarrinhoVisible;
    }
}

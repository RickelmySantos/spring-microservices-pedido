import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, inject, OnInit } from '@angular/core';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowRightFromBracket, faCartShopping } from '@fortawesome/free-solid-svg-icons';
import { SharedModule } from 'src/app/shared/shared.module';

import { BadgeModule } from 'primeng/badge';
import { TooltipModule } from 'primeng/tooltip';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CustomMenuItem, MenuItemComponent } from 'src/app/core/layout/menu-item/menu-item.component';
import { MENU } from 'src/app/menu';
import { CarrinhoComponent } from 'src/app/shared/components/carrinho/carrinho.component';
import { CarrinhoService } from 'src/app/shared/components/carrinho/service/carrinho.service';
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
            <div class="flex gap-7">
                <div>
                    <a (click)="carrinhoComponent.open()" class="cursor-pointer">
                        <fa-icon [icon]="['fas', 'cart-shopping']" class="text-2xl text-white"></fa-icon>
                        <p-badge *ngIf="(quantidadeItens$ | async) > 0" [value]="(quantidadeItens$ | async)!" severity="danger" styleClass="absolute -top-2 -right-3"></p-badge>
                    </a>
                    <app-carrinho #carrinhoComponent></app-carrinho>
                </div>
                <div>
                    <a (click)="logout()" pTooltip="Logout" tooltipPosition="bottom">
                        <fa-icon [icon]="['fas', 'arrow-right-from-bracket']" class="text-2xl text-white cursor-pointer hover:text-gray-300 mr-4"></fa-icon>
                    </a>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgIf, NgFor, MenuItemComponent, CarrinhoComponent, BadgeModule, TooltipModule],
})
export class MenuComponent implements OnInit {
    private readonly carrinhoService: CarrinhoService = inject(CarrinhoService);

    private readonly authService: AuthService = inject(AuthService);

    readonly quantidadeItens$ = this.carrinhoService.quantidadeItens$;

    public menu: CustomMenuItem[] = [];

    isCarrinhoVisible = false;

    constructor(library: FaIconLibrary) {
        library.addIcons(faCartShopping);
        library.addIcons(faArrowRightFromBracket);
    }
    ngOnInit(): void {
        this.menu = MENU.filter(item => {
            const rolesAllowed = item.rolesAllowed;

            if (!rolesAllowed || rolesAllowed.length === 0) {
                return true;
            }
            return this.authService.hasAnyRole(rolesAllowed);
        });
    }

    logout(): void {
        this.authService.logout();
    }

    openCarrinho(): void {
        this.isCarrinhoVisible = !this.isCarrinhoVisible;
    }
}

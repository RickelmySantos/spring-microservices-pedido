import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { SharedModule } from 'src/app/shared/shared.module';
import { MenuItemComponent } from '../menu-item/menu-item.component';

@Component({
    selector: 'app-menu',
    template: `
        <section class="menu-bar container">
            <div class="menu-bar__logo">
                <a href="/">Sabor Brasileiro</a>
            </div>

            <nav class="menu-bar__nav">
                <ul>
                    <ng-container *ngFor="let item of menu">
                        <app-menu-item [item]="item"></app-menu-item>
                    </ng-container>
                </ul>
            </nav>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor, MenuItemComponent],
})
export class MenuComponent {
    menu: MenuItem[] = [
        { label: 'Início', url: '#home', icon: 'pi pi-home' },
        { label: 'Cardápio', routerLink: '/menu', icon: 'pi pi-book' },
        { label: 'Sobre', url: '#about' },
        { label: 'Contato', url: '#contact' },
    ];
}

import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-header',
    template: `
        <header>
            <div class="container header-container">
                <div class="logo">
                    <i class="fas fa-utensils"></i>
                    <h1>Sabores do Brasil</h1>
                </div>

                <nav>
                    <ul>
                        <li><a routerLink="/#inicio">Início</a></li>
                        <li><a routerLink="/#cardapio">Cardápio</a></li>
                        <li><a routerLink="/#sobre">Sobre</a></li>
                        <li><a routerLink="/#contato">Contato</a></li>
                    </ul>
                </nav>

                <div class="menu-toggle" (click)="toggleMenu()">
                    <i class="fas fa-bars"></i>
                </div>
            </div>

            <div id="mobile-menu" [class.active]="menuOpen">
                <ul>
                    <li><a routerLink="/#inicio">Início</a></li>
                    <li><a routerLink="/#cardapio">Cardápio</a></li>
                    <li><a routerLink="/#sobre">Sobre</a></li>
                    <li><a routerLink="/#contato">Contato</a></li>
                </ul>
            </div>
        </header>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class HeaderComponent {
    menuOpen = false;

    toggleMenu() {
        this.menuOpen = !this.menuOpen;
    }
}

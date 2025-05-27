import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-header',
    template: `
        <header id="header">
            <div class="container header-container">
                <div class="logo">
                    <i class="fas fa-utensils"></i>
                    <span>Sabor Brasileiro</span>
                </div>
                <nav>
                    <ul>
                        <li><a href="#home">Inicio</a></li>
                        <li><a href="#menu">Cardapio</a></li>
                        <li><a href="#about">Sobre</a></li>
                        <li><a href="#contact">Contato</a></li>
                    </ul>
                </nav>
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

import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CardapioComponent } from 'src/app/modules/cardapio/cardapio.component';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-home',
    template: `
        <section id="home" class="hero">
            <div class="hero-content">
                <h1>Sabores Brasileiros Autênticos em Cada Mordida</h1>
                <p>Experimente os sabores vibrantes do Brasil com nossos pratos cuidadosamente elaborados com receitas tradicionais e os ingredientes mais frescos.</p>
                <div>
                    <a href="#menu" class="btn">Cardápio</a>
                    <a href="#contact" class="btn btn-outline">Reservas</a>
                </div>
            </div>
        </section>
        <app-cardapio></app-cardapio>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CardapioComponent],
})
export class HomeComponent {}

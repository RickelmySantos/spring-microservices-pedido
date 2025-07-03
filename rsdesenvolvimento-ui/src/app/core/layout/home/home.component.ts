import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CardapioComponent } from 'src/app/modules/cardapio/cardapio.component';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-home',
    template: `
        <section class="hero">
            <div class="hero__imagem">
                <img src="/assets/image/hero-image.png" alt="Comida Brasileira" />
            </div>

            <div class="container">
                <div class="content">
                    <h1>
                        Descubra os
                        <span class="highlight">Sabores</span>
                        do Brasil
                    </h1>
                    <p>Autêntica culinária brasileira com ingredientes frescos e receitas tradicionais.</p>
                    <a href="#menu" class="btn-hero">Explorar Cardápio</a>
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

import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CardapioComponent } from 'src/app/modules/cardapio/cardapio.component';
import { DepoimentoComponent } from 'src/app/shared/components/depoimentos/depoimento.component';
import { NewsletterComponent } from 'src/app/shared/components/newsletter/newsletter.component';
import { SobreComponent } from 'src/app/shared/components/sobre/sobre.component';
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
        <app-sobre></app-sobre>
        <app-depoimento></app-depoimento>
        <app-newsletter></app-newsletter>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CardapioComponent, SobreComponent, DepoimentoComponent, NewsletterComponent],
})
export class HomeComponent {}

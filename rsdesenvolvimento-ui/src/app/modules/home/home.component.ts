import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

@Component({
    selector: 'app-home',
    template: `
        <section class="hero" id="inicio">
            <div class="hero-content">
                <h2>Descubra os verdadeiros sabores do Brasil!</h2>
                <p>Autêntica culinária brasileira feita com amor e ingredientes frescos</p>
                <a href="#cardapio" class="btn btn-primary">Ver Cardápio</a>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [],
})
export class HomeComponent {}

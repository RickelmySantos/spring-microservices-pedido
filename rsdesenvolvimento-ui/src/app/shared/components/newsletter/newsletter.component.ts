import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-newsletter',
    template: `
        <section class="newsletter-secao">
            <div class="container">
                <h2 class="newsletter__titulo">Receba Nossas Novidades</h2>
                <p class="newsletter__subtitulo">Cadastre-se para receber promoções exclusivas, novidades do cardápio e eventos especiais.</p>

                <form class="newsletter__form" (submit)="onSubmit()">
                    <input type="email" placeholder="Seu melhor e-mail" class="newsletter__input" required />
                    <button type="submit" class="newsletter__botao">Cadastrar</button>
                </form>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class NewsletterComponent {
    constructor() {}

    onSubmit(): void {
        console.log('Formulário enviado!');
    }
}

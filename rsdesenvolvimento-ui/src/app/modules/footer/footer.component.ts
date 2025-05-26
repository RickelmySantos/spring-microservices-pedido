import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-footer',
    template: `
        <footer>
            <div class="container footer-container">
                <div class="footer-logo">
                    <i class="fas fa-utensils"></i>
                    <div>
                        <h2>Sabores do Brasil</h2>
                        <p>Autêntica culinária brasileira</p>
                    </div>
                </div>
                <div class="footer-copyright">
                    <p>&copy; 2023 Sabores do Brasil. Todos os direitos reservados.</p>
                    <p>
                        Desenvolvido com
                        <i class="fas fa-heart"></i>
                        para o Brasil
                    </p>
                </div>
            </div>
        </footer>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class FooterComponent {}

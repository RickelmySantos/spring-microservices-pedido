import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-sobre',
    template: `
        <section class="about" id="sobre">
            <div class="container about-container">
                <div class="about-img">
                    <img src="URL" alt="Restaurante" />
                </div>
                <div class="about-content">
                    <h2>Sobre Nós</h2>
                    <p>O Sabores do Brasil nasceu da paixão...</p>
                    <div class="features">
                        <div class="feature-icon">
                            <i class="fas fa-leaf"></i>
                        </div>
                        <div class="feature-icon orange">
                            <i class="fas fa-heart"></i>
                        </div>
                        <div class="feature-icon green">
                            <i class="fas fa-flag"></i>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class SobreComponent {}

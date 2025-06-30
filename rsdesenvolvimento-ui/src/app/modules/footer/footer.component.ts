import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ContatoComponent } from '../contato/contato.component';

@Component({
    selector: 'app-footer',
    template: `
        <!-- Footer -->
        <footer>
            <div class="container">
                <div class="footer-content">
                    <div class="footer-column">
                        <h4>Sobre nós</h4>
                        <p>O Sabor Brasileiro traz a autêntica culinária brasileira, oferecendo um gostinho das diversas tradições culinárias do Brasil em um ambiente acolhedor e acolhedor.</p>
                        <div class="social-links">
                            <a href="#"><i class="fa-brands fa-facebook"></i></a>
                            <a href="#"><i class="fa-brands fa-square-instagram"></i></a>
                            <a href="#"><i class="fa-brands fa-twitter"></i></a>
                        </div>
                    </div>

                    <div class="footer-column">
                        <h4>Quick Links</h4>
                        <ul class="footer-links">
                            <li><a href="#home">Home</a></li>
                            <li><a href="#menu">Menu</a></li>
                            <li><a href="#about">Sobre nós</a></li>
                            <li><a href="#contact">Contato</a></li>
                        </ul>
                    </div>

                    <div class="footer-column">
                        <h4>Informações de Contatos</h4>
                    </div>

                    <div class="footer-column">
                        <h4>Boletim informativo</h4>
                        <p>Assine nosso boletim informativo para ofertas especiais e atualizações.</p>
                        <form class="newsletter-form">
                            <div class="form-group">
                                <input type="email" placeholder="Seu Email" required />
                            </div>
                            <button type="submit" class="btn">Assine</button>
                        </form>
                    </div>
                </div>

                <div class="copyright">
                    <p>
                        &copy; 2025 Sabor Brasileiro. Todos os direitos reservados. Desenvolvido por
                        <i class="fas fa-heart" style="color: var(--accent);"></i>
                        RSDesenvolvimento
                    </p>
                </div>
            </div>
        </footer>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, ContatoComponent],
})
export class FooterComponent {}

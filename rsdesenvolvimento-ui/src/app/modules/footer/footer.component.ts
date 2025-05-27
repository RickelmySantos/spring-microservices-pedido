import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-footer',
    template: `
        <!-- Footer -->
        <footer>
            <div class="container">
                <div class="footer-content">
                    <div class="footer-column">
                        <h4>About Us</h4>
                        <p>Sabor Brasileiro brings authentic Brazilian cuisine to Miami, offering a taste of Brazil's diverse culinary traditions in a warm, welcoming atmosphere.</p>
                        <div class="social-links">
                            <a href="#"><i class="fab fa-facebook-f"></i></a>
                            <a href="#"><i class="fab fa-instagram"></i></a>
                            <a href="#"><i class="fab fa-twitter"></i></a>
                        </div>
                    </div>

                    <div class="footer-column">
                        <h4>Quick Links</h4>
                        <ul class="footer-links">
                            <li><a href="#home">Home</a></li>
                            <li><a href="#menu">Menu</a></li>
                            <li><a href="#about">About Us</a></li>
                            <li><a href="#contact">Contact</a></li>
                            <li><a href="#">Gallery</a></li>
                            <li><a href="#">Events</a></li>
                        </ul>
                    </div>

                    <div class="footer-column">
                        <h4>Contact Info</h4>
                        <p>
                            <i class="fas fa-map-marker-alt"></i>
                            123 Avenida Brasil, Miami, FL
                        </p>
                        <p>
                            <i class="fas fa-phone"></i>
                            (305) 555-0187
                        </p>
                        <p>
                            <i class="fas fa-envelope"></i>
                            <!-- info@saborbrasileiro.com -->
                        </p>
                    </div>

                    <div class="footer-column">
                        <h4>Newsletter</h4>
                        <p>Subscribe to our newsletter for special offers and updates.</p>
                        <form class="newsletter-form">
                            <div class="form-group">
                                <input type="email" placeholder="Your Email Address" required />
                            </div>
                            <button type="submit" class="btn">Subscribe</button>
                        </form>
                    </div>
                </div>

                <div class="copyright">
                    <p>
                        &copy; 2023 Sabor Brasileiro. All Rights Reserved. Designed with
                        <i class="fas fa-heart" style="color: var(--accent);"></i>
                        in Miami
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

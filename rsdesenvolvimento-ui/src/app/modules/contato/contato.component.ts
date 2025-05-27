import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-contato',
    template: `
        <!-- Contact Section -->
        <section id="contact" class="contact">
            <div class="container">
                <div class="section-title">
                    <h2>Contact Us</h2>
                    <p>We'd love to hear from you! Reach out for reservations, questions, or just to say hello.</p>
                </div>

                <div class="contact-container">
                    <div class="contact-info">
                        <h3>Visit Our Restaurant</h3>
                        <p>
                            <i class="fas fa-map-marker-alt"></i>
                            123 Avenida Brasil, Miami, FL 33139
                        </p>
                        <p>
                            <i class="fas fa-phone"></i>
                            (305) 555-0187
                        </p>
                        <p>
                            <i class="fas fa-envelope"></i>
                            <!-- info@saborbrasileiro.com -->
                        </p>

                        <h3>Opening Hours</h3>
                        <p>
                            <strong>Monday - Thursday:</strong>
                            11:30am - 10:00pm
                        </p>
                        <p>
                            <strong>Friday - Saturday:</strong>
                            11:30am - 11:00pm
                        </p>
                        <p>
                            <strong>Sunday:</strong>
                            12:00pm - 9:00pm
                        </p>

                        <h3>Follow Us</h3>
                        <div class="social-links">
                            <a href="#"><i class="fab fa-facebook-f"></i></a>
                            <a href="#"><i class="fab fa-instagram"></i></a>
                            <a href="#"><i class="fab fa-twitter"></i></a>
                            <a href="#"><i class="fab fa-yelp"></i></a>
                        </div>
                    </div>

                    <div class="contact-form">
                        <h3>Send Us a Message</h3>
                        <form id="contactForm">
                            <div class="form-group">
                                <label for="name">Full Name</label>
                                <input type="text" id="name" name="name" required />
                            </div>
                            <div class="form-group">
                                <label for="email">Email Address</label>
                                <input type="email" id="email" name="email" required />
                            </div>
                            <div class="form-group">
                                <label for="phone">Phone Number</label>
                                <input type="tel" id="phone" name="phone" />
                            </div>
                            <div class="form-group">
                                <label for="subject">Subject</label>
                                <select id="subject" name="subject">
                                    <option value="reservation">Reservation</option>
                                    <option value="question">Question</option>
                                    <option value="feedback">Feedback</option>
                                    <option value="other">Other</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="message">Message</label>
                                <textarea id="message" name="message" required></textarea>
                            </div>
                            <button type="submit" class="btn">Send Message</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor],
})
export class ContatoComponent {}

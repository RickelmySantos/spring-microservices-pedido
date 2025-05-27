import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-sobre',
    template: `
        <!-- About Section -->
        <section id="about" class="about">
            <div class="container">
                <div class="section-title">
                    <h2>Our Story</h2>
                    <p>Discover the passion and tradition behind Sabor Brasileiro's authentic culinary experience.</p>
                </div>

                <div class="about-content">
                    <div class="about-text">
                        <h3>A Taste of Brazil in Every Dish</h3>
                        <p>
                            Founded in 2015 by Chef Rodrigo Silva, Sabor Brasileiro was born from a desire to share the authentic flavors of Brazil with the world. Chef Rodrigo, a native of Salvador,
                            Bahia, brings decades of culinary expertise and family recipes passed down through generations.
                        </p>
                        <p>
                            Our restaurant is more than just a dining establishment - it's a celebration of Brazil's diverse culinary heritage. From the Amazon to the beaches of Rio, we bring together
                            flavors from all regions of this vibrant country.
                        </p>
                        <p>
                            We pride ourselves on using only the freshest ingredients, many imported directly from Brazil, to ensure an authentic experience. Our team of chefs combines traditional
                            techniques with modern presentation to create dishes that are both familiar and exciting.
                        </p>
                        <p>
                            At Sabor Brasileiro, we believe that food is about connection - to culture, to family, and to joy. We invite you to experience the warmth, color, and vibrancy of Brazilian
                            culture through our carefully crafted menu.
                        </p>
                    </div>
                    <div class="about-image">
                        <img src="https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Chef preparing Brazilian food" />
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

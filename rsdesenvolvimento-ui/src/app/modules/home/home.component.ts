import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CardapioComponent } from '../cardapio/cardapio.component';

@Component({
    selector: 'app-home',
    template: `
        <!-- <section class="hero" id="inicio">
            <div class="hero-content">
                <h2>Descubra os verdadeiros sabores do Brasil!</h2>
                <p>Autêntica culinária brasileira feita com amor e ingredientes frescos</p>
                <a href="#cardapio" class="btn btn-primary">Ver Cardápio</a>
            </div>
        </section>
        <app-cardapio></app-cardapio> -->
        <!-- Hero Section -->
        <section id="home" class="hero">
            <div class="hero-content">
                <h1>Authentic Brazilian Flavors in Every Bite</h1>
                <p>Experience the vibrant tastes of Brazil with our carefully crafted dishes made from traditional recipes and the freshest ingredients.</p>
                <div>
                    <a href="#menu" class="btn">View Menu</a>
                    <a href="#contact" class="btn btn-outline">Reservations</a>
                </div>
            </div>
        </section>

        <!-- Menu Section -->
        <section id="menu" class="menu">
            <div class="container">
                <div class="section-title">
                    <h2>Our Menu</h2>
                    <p>Discover the rich culinary traditions of Brazil through our carefully curated menu featuring dishes from all regions of the country.</p>
                </div>

                <div class="menu-categories">
                    <button class="active" data-category="all">All Items</button>
                    <button data-category="starters">Starters</button>
                    <button data-category="mains">Main Courses</button>
                    <button data-category="desserts">Desserts</button>
                    <button data-category="drinks">Drinks</button>
                </div>

                <div class="menu-items">
                    <!-- Starters -->
                    <div class="menu-item active" data-category="starters">
                        <img src="https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Pão de Queijo" />
                        <div class="menu-item-content">
                            <h3>Pão de Queijo</h3>
                            <span class="price">$6.99</span>
                            <p class="description">Traditional Brazilian cheese bread made with tapioca flour and Minas cheese, crispy on the outside and chewy on the inside.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <div class="menu-item active" data-category="starters">
                        <img src="https://images.unsplash.com/photo-1511690651692-7e258df116a2?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Coxinha" />
                        <div class="menu-item-content">
                            <h3>Coxinha</h3>
                            <span class="price">$7.50</span>
                            <p class="description">Teardrop-shaped croquettes filled with shredded chicken, cream cheese, and spices, coated in crispy breadcrumbs.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <!-- Main Courses -->
                    <div class="menu-item active" data-category="mains">
                        <img src="https://images.unsplash.com/photo-1551504739-ee602f0b7a0b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Feijoada" />
                        <div class="menu-item-content">
                            <h3>Feijoada Completa</h3>
                            <span class="price">$22.99</span>
                            <p class="description">Brazil's national dish - a rich black bean stew with various pork cuts, served with rice, collard greens, farofa, and orange slices.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <div class="menu-item active" data-category="mains">
                        <img src="https://images.unsplash.com/photo-1544025162-d76694265947?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Moqueca" />
                        <div class="menu-item-content">
                            <h3>Moqueca de Peixe</h3>
                            <span class="price">$24.50</span>
                            <p class="description">Traditional fish stew with coconut milk, dendê oil, tomatoes, onions, and cilantro, served with rice and pirão.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <div class="menu-item active" data-category="mains">
                        <img src="https://images.unsplash.com/photo-1565299507177-b0ac66763828?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Churrasco" />
                        <div class="menu-item-content">
                            <h3>Churrasco Misto</h3>
                            <span class="price">$26.99</span>
                            <p class="description">Mixed grill featuring picanha (top sirloin cap), linguiça (sausage), and frango (chicken) served with garlic bread and vinaigrette sauce.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <!-- Desserts -->
                    <div class="menu-item active" data-category="desserts">
                        <img src="https://images.unsplash.com/photo-1563805042-7684c019e1cb?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Brigadeiro" />
                        <div class="menu-item-content">
                            <h3>Brigadeiro</h3>
                            <span class="price">$5.99</span>
                            <p class="description">Classic Brazilian chocolate truffles made with condensed milk, cocoa powder, and butter, rolled in chocolate sprinkles.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <div class="menu-item active" data-category="desserts">
                        <img src="https://images.unsplash.com/photo-1558326567-98ae2405596b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Pudim" />
                        <div class="menu-item-content">
                            <h3>Pudim de Leite</h3>
                            <span class="price">$6.50</span>
                            <p class="description">Brazilian-style flan with a rich caramel sauce, made with condensed milk, eggs, and sugar.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <!-- Drinks -->
                    <div class="menu-item active" data-category="drinks">
                        <img src="https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Caipirinha" />
                        <div class="menu-item-content">
                            <h3>Caipirinha</h3>
                            <span class="price">$9.99</span>
                            <p class="description">Brazil's national cocktail made with cachaça, lime, sugar, and ice - refreshing with a kick!</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>

                    <div class="menu-item active" data-category="drinks">
                        <img src="https://images.unsplash.com/photo-1558645836-e44122a743ee?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80" alt="Guaraná" />
                        <div class="menu-item-content">
                            <h3>Guaraná Antarctica</h3>
                            <span class="price">$3.50</span>
                            <p class="description">Brazil's most popular soft drink made from the guaraná fruit, with a unique sweet and slightly tart flavor.</p>
                            <a href="#" class="add-to-cart">Add to Order</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [CardapioComponent],
})
export class HomeComponent {}

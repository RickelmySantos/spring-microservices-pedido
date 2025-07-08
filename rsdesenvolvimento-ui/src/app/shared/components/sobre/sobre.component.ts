import { NgFor, NgOptimizedImage } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faHeart, faLeaf, faSmile, faUtensils } from '@fortawesome/free-solid-svg-icons';
import { SharedModule } from 'src/app/shared/shared.module';

interface Diferencial {
    icone: IconDefinition;
    titulo: string;
    descricao: string;
}

@Component({
    selector: 'app-sobre',
    template: `
        <section id="sobre" class="sobre-secao">
            <div class="container">
                <div class="sobre__layout">
                    <div class="sobre__imagem-container">
                        <!-- <img
                            src="https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1374&q=80"
                            alt="Sobre Nós"
                            class="sobre__imagem" /> -->
                        <img ngSrc="image-sobre-section_o9wsy5" alt="Sobre Nós" class="sobre__imagem" width="600" height="450" />
                    </div>
                    <div class="sobre__texto-container">
                        <h2 class="titulo-secao">Nossa História</h2>
                        <p class="paragrafo">Fundado em 2010, o Sabores do Brasil nasceu da paixão pela gastronomia regional brasileira e do desejo de compartilhar essa riqueza cultural com todos.</p>
                        <p class="paragrafo">
                            Nossos chefs viajam por todo o país para aprender receitas tradicionais diretamente com as comunidades locais, garantindo autenticidade em cada prato que servimos.
                        </p>

                        <div class="diferenciais__grid">
                            <div *ngFor="let item of diferenciais" class="diferencial-item">
                                <div class="diferencial-item__icone-wrapper">
                                    <fa-icon [icon]="item.icone" class="text-white"></fa-icon>
                                </div>
                                <div class="diferencial-item__texto">
                                    <h4 class="diferencial-item__titulo">{{ item.titulo }}</h4>
                                    <p>{{ item.descricao }}</p>
                                </div>
                            </div>
                        </div>

                        <button class="botao-principal">Conheça Nossa Equipe</button>
                    </div>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor, NgOptimizedImage],
})
export class SobreComponent {
    diferenciais: Diferencial[] = [
        {
            icone: faUtensils,
            titulo: 'Ingredientes Frescos',
            descricao: 'Trabalhamos com produtores locais para garantir a melhor qualidade.',
        },
        {
            icone: faHeart,
            titulo: 'Preparo Tradicional',
            descricao: 'Respeitamos as técnicas e segredos das receitas originais.',
        },
        {
            icone: faLeaf,
            titulo: 'Sustentabilidade',
            descricao: 'Comprometidos com práticas sustentáveis em toda nossa cadeia.',
        },
        {
            icone: faSmile,
            titulo: 'Experiência Única',
            descricao: 'Cada prato conta uma história da cultura brasileira.',
        },
    ];

    constructor() {}
}

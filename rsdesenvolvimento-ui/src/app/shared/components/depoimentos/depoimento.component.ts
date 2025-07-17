import { NgFor, NgOptimizedImage } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { faStar as faStarRegular } from '@fortawesome/free-regular-svg-icons';
import { faStar, faStarHalfAlt } from '@fortawesome/free-solid-svg-icons';
import { SharedModule } from 'src/app/shared/shared.module';

interface Depoimento {
    estrelas: number;
    texto: string;
    autor: string;
    cargo: string;
    imagemUrl: string;
}

@Component({
    selector: 'app-depoimento',
    template: `
        <section id="depoimentos" class="depoimentos-secao">
            <div class="container">
                <div class="cabecalho-secao">
                    <h2 class="titulo-secao">O Que Dizem Nossos Clientes</h2>
                    <p class="subtitulo-secao">A satisfação dos nossos clientes é o nosso maior orgulho.</p>
                </div>

                <div class="depoimentos__grid">
                    <div *ngFor="let depoimento of depoimentos" class="cartao-depoimento">
                        <div class="cartao-depoimento__estrelas">
                            <fa-icon *ngFor="let icone of getEstrelas(depoimento.estrelas)" [icon]="icone"></fa-icon>
                        </div>
                        <p class="cartao-depoimento__texto">"{{ depoimento.texto }}"</p>
                        <div class="cartao-depoimento__autor-info">
                            <img [ngSrc]="depoimento.imagemUrl" [alt]="'Foto de ' + depoimento.autor" class="autor-info__imagem" width="33" height="33" />
                            <div>
                                <h4 class="autor-info__nome">{{ depoimento.autor }}</h4>
                                <p class="autor-info__cargo">{{ depoimento.cargo }}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="rodape-secao">
                    <button class="botao-principal">Deixe Seu Depoimento</button>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor, NgOptimizedImage],
})
export class DepoimentoComponent {
    iconeEstrelaCheia = faStar;
    iconeMeiaEstrela = faStarHalfAlt;
    iconeEstrelaVazia = faStarRegular;

    depoimentos: Depoimento[] = [
        {
            estrelas: 5,
            texto: 'A experiência no Sabores do Brasil foi incrível! O pato no tucupi estava perfeito, exatamente como lembrava da minha viagem ao Pará. Ambiente aconchegante e atendimento impecável.',
            autor: 'Ana Carolina',
            cargo: 'Food Blogger',
            imagemUrl: '32_awjwrn',
        },
        {
            estrelas: 5,
            texto: 'Como nordestino que mora longe de casa, encontrar um acarajé tão autêntico aqui em São Paulo foi emocionante. O sabor me transportou direto para a Bahia. Recomendo muito!',
            autor: 'Carlos Eduardo',
            cargo: 'Advogado',
            imagemUrl: '75_pkg2bb',
        },
        {
            estrelas: 4.5,
            texto: 'Levei uns amigos estrangeiros para conhecer a culinária brasileira e ficaram maravilhados! O rodízio de pratos regionais foi perfeito para mostrar a diversidade do nosso país. Voltaremos com certeza!',
            autor: 'Juliana Santos',
            cargo: 'Guia de Turismo',
            imagemUrl: '63_dix2bn',
        },
    ];

    constructor() {}

    getEstrelas(nota: number): any[] {
        const estrelas = [];
        for (let i = 1; i <= 5; i++) {
            if (i <= nota) {
                estrelas.push(this.iconeEstrelaCheia);
            } else if (i - 0.5 === nota) {
                estrelas.push(this.iconeMeiaEstrela);
            } else {
                estrelas.push(this.iconeEstrelaVazia);
            }
        }
        return estrelas;
    }
}

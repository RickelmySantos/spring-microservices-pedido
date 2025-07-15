import { CurrencyPipe, NgOptimizedImage } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Input, Output } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-card',
    template: `
        <article class="cardapio-card" [attr.data-category]="item.categoria">
            <figure class="cardapio-card__figure">
                <img [ngSrc]="item.imagemUrl" [alt]="item.alt" class="cardapio-card__image" width="300" height="174" />
                <span class="cardapio-card__badge">{{ item.categoria }}</span>
            </figure>

            <section class="cardapio-card__body">
                <header class="cardapio-card__header">
                    <h3 class="cardapio-card__title">{{ item.nome }}</h3>
                    <span class="cardapio-card__price">{{ item.preco | currency : 'BRL' }}</span>
                </header>

                <p class="cardapio-card__description">{{ item.observacao }}</p>

                <button class="cardapio-card__action-button" (click)="onAdicionar()" [attr.aria-label]="'Adicionar ' + item.nome + ' ao pedido'">
                    <span>Adicionar</span>
                </button>
            </section>
        </article>
    `,
    standalone: true,

    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CurrencyPipe, NgOptimizedImage],
})
export class CardapioCardComponent {
    @Input()
    item!: MenuCardapio;

    @Output()
    adiconarProduto = new EventEmitter<MenuCardapio>();

    onAdicionar(): void {
        this.adiconarProduto.emit(this.item);
    }
}

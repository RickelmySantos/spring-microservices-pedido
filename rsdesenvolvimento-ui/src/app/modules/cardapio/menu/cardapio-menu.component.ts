import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Input, Output } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-menu',
    template: `
        <article class="cardapio-menu-item" [attr.data-category]="item.categoria">
            <section class="cardapio-menu-item__image-container">
                <img [src]="item.imagemUrl" [alt]="item.alt" class="cardapio-menu-item__image" />
                <div class="cardapio-menu-item__category-tag">
                    {{ item.categoria }}
                </div>
            </section>
            <section class="cardapio-menu-item__content">
                <div class="cardapio-menu-item__header">
                    <h3 class="cardapio-menu-item__title">{{ item.nome }}</h3>
                    <span class="cardapio-menu-item__price">{{ item.preco }}</span>
                </div>
                <p class="cardapio-menu-item__description">{{ item.descricao }}</p>
                <button class="cardapio-menu-item__add-button" (click)="onAdicionar()" [attr.aria-label]="'Adicionar ' + item.nome + ' ao pedido'">
                    <!-- <fa-icon [icon]="faCartPlus"></fa-icon> -->
                    <span>Adicionar</span>
                </button>
            </section>
        </article>
    `,
    standalone: true,

    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class CardapioMenuComponent {
    @Input()
    item!: MenuCardapio;

    @Output()
    adiconarProduto = new EventEmitter<MenuCardapio>();

    onAdicionar(): void {
        this.adiconarProduto.emit(this.item);
    }
}

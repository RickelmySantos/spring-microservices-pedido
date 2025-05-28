import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-menu',
    template: `
        <div class=" menu-item active" [attr.data-categoria]="item.categoria">
            <img [src]="item.imagem" [alt]="item.alt" />
            <div class="menu-item-content">
                <h3>{{ item.titulo }}</h3>
                <span class="price">{{ item.preco }}</span>
                <p class="description">{{ item.descricao }}</p>
                <a href="#" class="add-to-cart">Adcionar ao Pedido</a>
            </div>
        </div>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class CardapioMenuComponent {
    @Input()
    item!: MenuCardapio;
}

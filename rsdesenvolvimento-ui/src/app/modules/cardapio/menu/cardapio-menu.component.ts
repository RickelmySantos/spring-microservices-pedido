import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { PedidoService } from 'src/app/services/pedido.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-menu',
    template: `
        <div class=" menu-item active" [attr.data-categoria]="item.categoria">
            <img [src]="item.imagemUrl" [alt]="item.alt" />
            <div class="menu-item-content">
                <h3>{{ item.nome }}</h3>
                <span class="price">{{ item.preco }}</span>
                <p class="description">{{ item.descricao }}</p>
                <a class="add-to-cart" (click)="fazerPedido()">Adcionar ao Pedido</a>
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
    // pedido: Pedido;

    constructor(private pedidoService: PedidoService, private usuarioService: UsuarioService) {}

    fazerPedido() {}
}

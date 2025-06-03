import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { PedidoService } from 'src/app/services/pedido.service';
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

    constructor(private pedidoService: PedidoService) {}

    pedido = {
        descricao: 'Pedido de Picanha Grelhada e Suco Natural Laranja',
        usuarioId: 2,
        itens: [{ produtoId: 1, quantidade: 1 }],
    };
    fazerPedido() {
        this.pedidoService.registrarPedido(this.pedido).subscribe(
            response => {
                console.log('Pedido registrado com sucesso!', response);
            },
            error => {
                console.error('Erro ao registrar pedido', error);
            }
        );
    }
}

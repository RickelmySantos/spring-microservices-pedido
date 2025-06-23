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
                <a class="add-to-cart" (click)="realizarPedido()">Adcionar ao Pedido</a>
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

    realizarPedido() {
        const pedido = {
            descricao: `Pedido de ${this.item.nome}`,
            itens: [
                {
                    produtoId: this.item.id,
                    quantidade: 1,
                },
            ],
        };
        this.pedidoService.registrarPedido(pedido).subscribe({
            next: response => {
                console.log('Pedido realizado com sucesso:', response);
            },
            error: error => {
                console.error('Erro ao realizar o pedido:', error);
            },
        });
    }
}

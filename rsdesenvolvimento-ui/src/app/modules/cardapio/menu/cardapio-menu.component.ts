import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { PedidoService } from 'src/app/services/pedido.service';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-menu',
    template: `
        <div class="cardapio-menu-item" [attr.data-categoria]="item.categoria">
            <img class="menu-item__image" [src]="item.imagemUrl" [alt]="item.alt" />

            <div class="cardapio-menu-item__content">
                <h3 class="cardapio-menu-item__title">{{ item.nome }}</h3>

                <p class="cardapio-menu-item__description">{{ item.descricao }}</p>

                <div class="cardapio-menu-item__footer">
                    <span class="cardapio-menu-item__price">{{ item.preco }}</span>
                    <button type="button" class="cardapio-menu-item__add-to-cart-btn" (click)="realizarPedido()">Adicionar</button>
                </div>
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

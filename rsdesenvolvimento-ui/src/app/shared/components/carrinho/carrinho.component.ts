import { CurrencyPipe, NgFor, NgIf } from '@angular/common';
import { Component, CUSTOM_ELEMENTS_SCHEMA, inject, Input, OnInit } from '@angular/core';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { CarrinhoService } from 'src/app/services/carrinho.service';
import { PedidoService } from 'src/app/services/pedido.service';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-carrinho',
    template: `
        <div class="modal-backdrop" *ngIf="isVisible" (click)="close()"></div>

        <div class="modal" *ngIf="isVisible">
            <div class="modal-content">
                <h2>Seu Pedido</h2>

                <div class="carrinho-itens">
                    <div *ngFor="let item of carrinhoItem" class="carrinho-item">
                        <img [src]="item.imagemUrl" [alt]="item.nome" class="item-img" />

                        <div class="item-info">
                            <h3>{{ item.nome }}</h3>
                            <p>{{ item.preco | currency : 'BRL' }}</p>
                            <div class="item-controle">
                                <button (click)="alterarQuantidade(item, -1)">–</button>
                                <span>{{ item.quantidade }}</span>
                                <button (click)="alterarQuantidade(item, 1)">+</button>
                            </div>
                        </div>

                        <div class="item-total">
                            {{ calcularSubtotal(item) | currency : 'BRL' }}
                        </div>
                    </div>
                </div>

                <div class="carrinho-total">
                    <strong>Total:</strong>
                    {{ calcularTotal() | currency : 'BRL' }}
                </div>

                <div class="carrinho-acoes">
                    <button (click)="finalizarPedido()">Finalizar Pedido</button>
                    <button (click)="close()">Fechar</button>
                </div>
            </div>
        </div>
    `,
    styleUrl: './carrinho.component.scss',
    standalone: true,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgIf, NgFor, CurrencyPipe],
})
export class CarrinhoComponent implements OnInit {
    protected readonly carrinhoService: CarrinhoService = inject(CarrinhoService);
    protected readonly pedidoService: PedidoService = inject(PedidoService);

    @Input() carrinhoItem: any[] = [];
    isVisible = false;

    ngOnInit(): void {
        this.carrinhoService.itensCarrinho$.subscribe(itens => {
            this.carrinhoItem = itens;
        });
    }

    open() {
        console.log('Abrindo modal do carrinho');
        this.isVisible = true;
    }

    close() {
        this.isVisible = false;
    }

    finalizarPedido(): void {
        const pedido = {
            descricao: 'Pedido de múltiplos itens',
            item: this.carrinhoItem.map(i => ({
                produtoId: i.id,
                quantidade: i.quantidade || 1,
            })),
        };

        this.pedidoService.registrarPedido(pedido).subscribe({
            next: () => {
                alert('Pedido realizado com sucesso! 🍽️');
                this.carrinhoService.limparCarrinho();
                this.close();
            },
            error: () => {
                alert('Erro ao finalizar o pedido, senhor...');
            },
        });
    }

    alterarQuantidade(item: ItemCarrinho, delta: number): void {
        item.quantidade += delta;
        if (item.quantidade <= 0) {
            this.carrinhoItem = this.carrinhoItem.filter(i => i.id !== item.id);
        }
        this.carrinhoService.atualizarCarrinho([...this.carrinhoItem]);
    }

    calcularSubtotal(item: ItemCarrinho): number {
        return parseFloat(item.preco) * item.quantidade;
    }

    calcularTotal(): number {
        return this.carrinhoItem.reduce((total, item) => {
            return total + parseFloat(item.preco) * item.quantidade;
        }, 0);
    }
}

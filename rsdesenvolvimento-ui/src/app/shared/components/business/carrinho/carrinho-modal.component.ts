import { CurrencyPipe, NgFor, NgIf, NgOptimizedImage } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AlterarQuantidadeEvent } from 'src/app/models/alterarQuantidade.model';

import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-carrinho-modal',
    template: `
        <div class="modal-backdrop" *ngIf="isVisible" (click)="fechar.emit()"></div>

        <dialog class="modal" *ngIf="isVisible" [open]="true" aria-labelledby="modal-titulo" aria-modal="true">
            <div class="modal-content">
                <h2 id="modal-titulo">Seu Pedido</h2>

                <ul class="carrinho-itens">
                    <li *ngFor="let item of itens" class="carrinho-item">
                        <!-- <img [src]="item.imagemUrl" [alt]="item.nome" class="item-img" /> -->
                        <div class="item-img-container">
                            <img [ngSrc]="item.imagemUrl" [alt]="item.nome" class="item-img" width="80" height="60" />
                        </div>

                        <div class="item-info">
                            <h3>{{ item.nome }}</h3>
                            <p>{{ item.preco | currency : 'BRL' }}</p>
                            <div class="item-controle">
                                <button (click)="alterarQuantidade.emit({ item: item, delta: -1 })" [disabled]="item.quantidade <= 1" [attr.aria-label]="'Diminuir quantidade do item ' + item.nome">
                                    –
                                </button>
                                <span>{{ item.quantidade }}</span>
                                <button (click)="alterarQuantidade.emit({ item: item, delta: 1 })" [attr.aria-label]="'Aumentar quantidade do item ' + item.nome">+</button>
                            </div>
                        </div>

                        <div class="item-total">
                            {{ calcularSubtotal(item) | currency : 'BRL' }}
                        </div>
                    </li>
                    <div *ngIf="!itens || itens.length === 0" class="carrinho-vazio">
                        <p>Seu carrinho está vazio. 🛒</p>
                    </div>
                </ul>

                <dl class="carrinho-total">
                    <dt>Total:</dt>
                    <dd>{{ total | currency : 'BRL' }}</dd>
                </dl>

                <footer class="carrinho-acoes">
                    <button (click)="finalizarPedido.emit()" [disabled]="isLoading || !itens || itens.length === 0">
                        {{ isLoading ? 'Finalizando...' : 'Finalizar Pedido' }}
                    </button>
                    <button class="secondary" (click)="fechar.emit()">Fechar</button>
                </footer>
            </div>
        </dialog>
    `,

    standalone: true,
    imports: [SharedModule, NgFor, NgIf, CurrencyPipe, NgOptimizedImage],
})
export class CarrinhoModalComponent {
    @Input() itens: ItemCarrinho[] | null = [];
    @Input() total: number | null = 0;
    @Input() isVisible = false;
    @Input() isLoading = false;

    @Output() fechar = new EventEmitter<void>();
    @Output() finalizarPedido = new EventEmitter<void>();
    @Output() alterarQuantidade = new EventEmitter<AlterarQuantidadeEvent>();

    calcularSubtotal(item: ItemCarrinho): number {
        return item.preco * item.quantidade;
    }
}

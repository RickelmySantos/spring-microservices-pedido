import { Component, CUSTOM_ELEMENTS_SCHEMA, inject } from '@angular/core';
import { AlterarQuantidadeEvent } from 'src/app/models/alterarQuantidade.model';
import { CheckoutService } from 'src/app/services/checkout.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { CarrinhoModalComponent } from './carrinho-modal.component';
import { CarrinhoService } from './service/carrinho.service';

@Component({
    selector: 'app-carrinho',
    template: `
        <app-carrinho-modal
            [itens]="itensCarrinho$ | async"
            [total]="totalCarrinho$ | async"
            [isVisible]="isVisible"
            [isLoading]="isLoading"
            (fechar)="onFecharModal()"
            (finalizarPedido)="onFinalizarPedido()"
            (alterarQuantidade)="onAlterarQuantidade($event)"></app-carrinho-modal>
    `,
    standalone: true,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, CarrinhoModalComponent],
})
export class CarrinhoComponent {
    protected readonly carrinhoService: CarrinhoService = inject(CarrinhoService);
    protected readonly checkoutService: CheckoutService = inject(CheckoutService);

    protected readonly itensCarrinho$ = this.carrinhoService.items$;
    protected readonly totalCarrinho$ = this.carrinhoService.totalCarrinho$;

    public isVisible = false;
    public isLoading = false;

    open(): void {
        this.isVisible = true;
    }

    onFecharModal(): void {
        this.isVisible = false;
    }

    onFinalizarPedido(): void {
        this.isLoading = true;
        this.checkoutService.finalizarPedido().subscribe({
            next: () => {
                this.isLoading = false;
                alert('Pedido realizado com sucesso! 🎉');
                this.onFecharModal();
            },
            error: err => {
                this.isLoading = false;
                alert(err.message);
            },
        });
    }
    onAlterarQuantidade(event: AlterarQuantidadeEvent): void {
        this.carrinhoService.incrementarQuantidadeOuRemover(event.item.id, event.delta);
    }
}

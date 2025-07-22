import { inject, Injectable, InjectionToken } from '@angular/core';
import { catchError, first, Observable, switchMap, tap, throwError } from 'rxjs';
import { CriarPedidoRequest } from '../models/criarPedido.model';
import { Pedido } from '../models/pedido.model';

import { ICarrinhoService } from './interfaces/carrinho.interface';
import { IPedidoService } from './interfaces/pedido.interface';

export const CARRINHO_SERVICE_TOKEN = new InjectionToken<ICarrinhoService>('ICarrinhoService');
export const PEDIDO_SERVICE_TOKEN = new InjectionToken<IPedidoService>('IPedidoService');

@Injectable({ providedIn: 'root' })
export class CheckoutService {
    private readonly carrinhoService: ICarrinhoService = inject(CARRINHO_SERVICE_TOKEN);
    private readonly pedidoService: IPedidoService = inject(PEDIDO_SERVICE_TOKEN);

    finalizarPedido(): Observable<Pedido> {
        return this.carrinhoService.itensCarrinho$.pipe(
            first(),
            switchMap(itens => {
                if (!itens.length) {
                    return throwError(() => new Error('Seu carrinho está vazio... 😔'));
                }

                const payload: CriarPedidoRequest = {
                    observacao: 'Pedido realizado via checkout',
                    itensPedido: itens.map(item => ({
                        produtoId: item.id,
                        quantidade: item.quantidade,
                        precoUnitario: item.preco,
                        nomeProduto: item.nome,
                    })),
                };
                return this.pedidoService.registrarPedido(payload).pipe(tap(() => this.carrinhoService.limparCarrinho()));
            }),
            catchError(err => {
                console.error('Erro ao finalizar pedido:', err);
                return throwError(() => new Error('Erro ao finalizar pedido. Tente novamente.'));
            })
        );
    }
}

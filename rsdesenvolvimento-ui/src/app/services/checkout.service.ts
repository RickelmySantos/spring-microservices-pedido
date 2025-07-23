import { Injectable } from '@angular/core';
import { catchError, first, Observable, switchMap, tap, throwError } from 'rxjs';
import { CriarPedidoRequest } from '../models/criarPedido.model';
import { Pedido } from '../models/pedido.model';
import { CarrinhoService } from './carrinho.service';
import { PedidoService } from './pedido.service';

@Injectable({ providedIn: 'root' })
export class CheckoutService {
    constructor(private readonly carrinhoService: CarrinhoService, private readonly pedidoService: PedidoService) {}

    finalizarPedido(): Observable<Pedido> {
        return this.carrinhoService.items$.pipe(
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
                return this.pedidoService.registrarPedido(payload).pipe(tap(() => this.carrinhoService.clear()));
            }),
            catchError(err => {
                console.error('Erro ao finalizar pedido:', err);
                return throwError(() => new Error('Erro ao finalizar pedido. Tente novamente.'));
            })
        );
    }
}

import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { CrudService } from 'src/app/core/services/crud.service';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';
import { Pedido } from 'src/app/models/pedido.model';

@Injectable({
    providedIn: 'root',
})
export class PedidoService extends CrudService<Pedido> {
    protected override PATH: string = 'pedido-service/api/pedido';

    constructor() {
        super();
    }

    registrarPedido(pedido: Partial<Pedido>): Observable<Pedido> {
        return this.http.post<Pedido>(this.getUrl(), pedido).pipe(
            tap(pedidoCriado => {
                console.log(`[PedidoService] Pedido #${pedidoCriado.id} registrado com sucesso!`);
            }),
            catchError(err => {
                console.error(`[PedidoService] Erro ao registrar pedido: ${err.message}`);
                return throwError(() => new Error('Erro ao registrar o pedido. Tente novamente mais tarde.'));
            })
        );
    }

    criarPedido(item: MenuCardapio): Observable<Pedido> {
        const novoPedido = {
            observacao: `Pedido de ${item.nome}`,
            itens: [
                {
                    produtoId: item.id,
                    quantidade: 1,
                },
            ],
        };
        return this.registrarPedido(novoPedido);
    }
}

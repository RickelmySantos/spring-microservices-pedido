import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { CrudService } from 'src/app/core/services/crud.service';
import { CriarPedidoRequest } from 'src/app/models/criarPedido.model';
import { Pedido } from 'src/app/models/pedido.model';
import { IPedidoService } from './interfaces/pedido.interface';

@Injectable({
    providedIn: 'root',
})
export class PedidoService extends CrudService<Pedido> implements IPedidoService {
    protected override PATH: string = 'pedido-service/api/pedido';

    registrarPedido(pedido: CriarPedidoRequest): Observable<Pedido> {
        return this.http.post<Pedido>(this.getUrl(), pedido).pipe(
            tap(pedidoCriado => {
                console.log(`[PedidoService] Payload enviado:`, pedido);
                console.log(`[PedidoService] Pedido #${pedidoCriado.id} registrado com sucesso!`);
            }),
            catchError(err => {
                console.error(`[PedidoService] Erro ao registrar pedido: ${err.message}`);
                return throwError(() => new Error('Erro ao registrar o pedido. Tente novamente mais tarde.'));
            })
        );
    }
}

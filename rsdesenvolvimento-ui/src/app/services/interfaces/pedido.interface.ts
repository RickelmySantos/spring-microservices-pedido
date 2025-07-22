import { Observable } from 'rxjs';
import { CriarPedidoRequest } from 'src/app/models/criarPedido.model';
import { Pedido } from 'src/app/models/pedido.model';

export interface IPedidoService {
    registrarPedido(payload: CriarPedidoRequest): Observable<Pedido>;
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class PedidoService {
    private readonly API_URL = 'http://localhost:8080/pedido-service/api/pedido';

    constructor(private http: HttpClient) {}

    registrarPedido(pedido: any) {
        return this.http.post(this.API_URL, pedido);
    }
}

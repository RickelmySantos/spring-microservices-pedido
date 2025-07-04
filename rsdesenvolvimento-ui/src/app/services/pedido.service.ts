import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Observable } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';

@Injectable({
    providedIn: 'root',
})
export class PedidoService {
    private readonly API_URL = 'http://localhost:8080/pedido-service/api/pedido';

    constructor(private http: HttpClient, private oauthService: OAuthService) {}

    registrarPedido(pedido: any) {
        const token = this.oauthService.getAccessToken();

        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
        console.log('Enviando para API:', this.API_URL, pedido);

        return this.http.post(this.API_URL, pedido, { headers });
    }

    criarPedido(item: MenuCardapio): Observable<any> {
        const novoPedido = {
            descricao: `Pedido de ${item.nome}`,
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

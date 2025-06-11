import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
    providedIn: 'root',
})
export class PedidoService {
    private readonly API_URL = 'http://localhost:8080/pedido-service/api/pedido';

    constructor(private http: HttpClient, private oauthService: OAuthService) {}

    registrarPedido(pedido: any) {
        const token = this.oauthService.getAccessToken();
        console.log('Token enviado:', token);

        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
        return this.http.post(this.API_URL, pedido, { headers });
    }
}

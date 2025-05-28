import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';

@Injectable({ providedIn: 'root' })
export class EstoqueService {
    private readonly API = 'http://localhost:8080/api/estoque';

    constructor(private http: HttpClient) {}

    listarProdutos(categoria?: string): Observable<MenuCardapio[]> {
        let params = new HttpParams();
        if (categoria) {
            params = params.set('categoria', categoria);
        }
        return this.http.get<MenuCardapio[]>(this.API, { params }).pipe(take(1));
    }
}

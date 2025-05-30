import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { MenuCardapio } from 'src/app/models/menu-cardapio.model';

@Injectable({ providedIn: 'root' })
export class EstoqueService {
    private readonly API = 'http://localhost:8080/estoque-api/api/estoque';

    constructor(private http: HttpClient) {}

    listarProdutos(categoria?: string): Observable<MenuCardapio[]> {
        let params = new HttpParams();
        if (categoria) {
            params = params.set('categoria', categoria);
        }
        return this.http.get<MenuCardapio[]>(this.API, { params }).pipe(take(1));
    }

    uploadImagem(file: File): Observable<string> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post(`${this.API}/upload`, formData, { responseType: 'text' }).pipe(take(1));
    }

    salvarProduto(dados: any, imagem: File): Observable<any> {
        const formData = new FormData();
        formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }));
        formData.append('imagem', imagem);

        return this.http.post<any>(this.API, formData);
    }
}

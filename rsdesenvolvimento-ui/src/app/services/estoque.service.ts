import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { CrudService } from 'src/app/core/services/crud.service';
import { Produto } from '../models/produto.model';

@Injectable({ providedIn: 'root' })
export class EstoqueService extends CrudService<Produto> {
    protected override PATH: string = 'estoque-api/api/estoque';
    // private readonly API = 'http://localhost:8080/estoque-api/api/estoque';

    listarProdutos(categoria?: string): Observable<Produto[]> {
        let params = new HttpParams();
        if (categoria) {
            params = params.set('categoria', categoria);
        }
        return this.http.get<Produto[]>(this.getUrl(), { params }).pipe(take(1));
    }

    uploadImagem(file: File): Observable<string> {
        const formData = new FormData();
        formData.append('file', file);
        return this.http.post(`${this.getUrl()}/upload`, formData, { responseType: 'text' }).pipe(take(1));
    }

    salvarProduto(dados: Omit<Produto, 'id'>, imagem: File): Observable<Produto> {
        const formData = new FormData();
        formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }));
        formData.append('imagem', imagem);

        return this.http.post<Produto>(this.getUrl(), formData);
    }
}

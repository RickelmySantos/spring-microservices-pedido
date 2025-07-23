import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Produto } from '../../models/produto.model';
import { EstoqueService } from '../estoque.service';
import { IProdutoRepository } from '../interfaces/cardapio.interfaces';

@Injectable({ providedIn: 'root' })
export class ProdutoRepositoryAdapter implements IProdutoRepository {
    constructor(private readonly estoqueService: EstoqueService) {}

    public listarProdutos(): Observable<Produto[]> {
        return this.estoqueService.listarProdutos();
    }
}

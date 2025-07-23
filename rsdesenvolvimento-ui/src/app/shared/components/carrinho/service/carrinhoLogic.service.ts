import { map, Observable } from 'rxjs';
import { ItemCarrinho } from 'src/app/models/itemCarrinho.model';
import { Produto } from 'src/app/models/produto.model';
import { CarrinhoBaseService, IStorageService } from './carrinhoBase.service';

export abstract class CarrinhoLogic extends CarrinhoBaseService<ItemCarrinho> {
    public readonly totalCarrinho$: Observable<number>;
    public readonly quantidadeItens$: Observable<number>;

    constructor(storageService: IStorageService<ItemCarrinho>, storageKey: string) {
        super(storageService, storageKey);

        this.totalCarrinho$ = this.items$.pipe(map(itens => itens.reduce((total, item) => total + item.preco * item.quantidade, 0)));

        this.quantidadeItens$ = this.items$.pipe(map(itens => itens.reduce((total, item) => total + item.quantidade, 0)));
    }

    public adcionarProduto(produto: Produto): void {
        const itemExistente = this.state.findItem(produto.id);

        if (itemExistente) {
            this.state.updateItem(produto.id, { quantidade: itemExistente.quantidade + 1 });
        } else {
            const novoItem: ItemCarrinho = {
                ...produto,
                quantidade: 1,
            };
            this.state.addItem(novoItem);
        }
    }

    public incrementarQuantidadeOuRemover(itemId: number, delta: number): void {
        const item = this.state.findItem(itemId);
        if (!item) return;

        const novaQuantidade = item.quantidade + delta;

        if (novaQuantidade > 0) {
            this.state.updateItem(itemId, { quantidade: novaQuantidade });
        } else {
            this.state.removeItem(itemId);
        }
    }
    public getQuantidadeProduto(produtoId: number): number {
        return this.state.findItem(produtoId)?.quantidade ?? 0;
    }
}

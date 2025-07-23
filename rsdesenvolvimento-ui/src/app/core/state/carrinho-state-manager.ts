import { ItemCarrinho } from '../../models/itemCarrinho.model';
import { Produto } from '../../models/produto.model';
import { StateManager } from './state-manager';

export class CarrinhoStateManager extends StateManager<ItemCarrinho[]> {
    constructor(itensIniciais: ItemCarrinho[] = []) {
        super(itensIniciais);
    }

    public adicionarItem(produto: Produto): void {
        this.updateState(itensAtuais => {
            const itens = [...itensAtuais];
            const itemExistente = itens.find(item => item.id === produto.id);

            if (itemExistente) {
                itemExistente.quantidade++;
            } else {
                itens.push({ ...produto, quantidade: 1 });
            }

            return itens;
        });
    }

    public incrementarQuantidadeOuRemover(itemId: number, delta: number): void {
        this.updateState(itensAtuais => {
            return itensAtuais
                .map(item => {
                    if (item.id === itemId) {
                        const novaQuantidade = item.quantidade + delta;
                        if (novaQuantidade <= 0) {
                            return null;
                        }
                        return { ...item, quantidade: novaQuantidade };
                    }
                    return item;
                })
                .filter((item): item is ItemCarrinho => item !== null);
        });
    }

    public removerItem(itemId: number): void {
        this.updateState(itensAtuais => itensAtuais.filter(item => item.id !== itemId));
    }

    public limparCarrinho(): void {
        this.setState([]);
    }

    public atualizarCarrinho(novosItens: ItemCarrinho[]): void {
        this.setState([...novosItens]);
    }

    public get isVazio(): boolean {
        return this.currentValue.length === 0;
    }

    public get quantidadeTotal(): number {
        return this.currentValue.reduce((total, item) => total + item.quantidade, 0);
    }

    public get valorTotal(): number {
        return this.currentValue.reduce((total, item) => total + item.preco * item.quantidade, 0);
    }

    public contemItem(produtoId: number): boolean {
        return this.currentValue.some(item => item.id === produtoId);
    }

    public getQuantidadeItem(produtoId: number): number {
        const item = this.currentValue.find(item => item.id === produtoId);
        return item ? item.quantidade : 0;
    }
}

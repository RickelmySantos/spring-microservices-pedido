import { StateManager } from './state-manager';

export class CategoriaStateManager extends StateManager<string> {
    constructor(initialCategoria: string = 'all') {
        super(initialCategoria);
    }

    public selecionarCategoria(categoria: string): void {
        this.setState(categoria);
    }

    public resetarCategoria(): void {
        this.setState('all');
    }

    public get isTodasCategorias(): boolean {
        return this.currentValue === 'all';
    }
}

import { BehaviorSubject, Observable } from 'rxjs';

export class CategoriaStateManager {
    private readonly state$: BehaviorSubject<string>;
    public readonly value$: Observable<string>;

    constructor(initialCategoria: string = 'all') {
        this.state$ = new BehaviorSubject<string>(initialCategoria);
        this.value$ = this.state$.asObservable();
    }

    public get value(): string {
        return this.state$.getValue();
    }

    public selecionarCategoria(categoria: string): void {
        this.state$.next(categoria);
    }

    public resetarCategoria(): void {
        this.state$.next('all');
    }

    public get isTodasCategorias(): boolean {
        return this.value === 'all';
    }

    public destroy(): void {
        this.state$.complete();
    }
}

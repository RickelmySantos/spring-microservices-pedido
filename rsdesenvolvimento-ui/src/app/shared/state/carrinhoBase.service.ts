import { Injectable } from '@angular/core';
import { map, Observable, takeUntil, tap } from 'rxjs';
import { Entity } from '../../core/components/models/entity.model';
import { Unsubscribe } from '../../core/util/unsubscribe';
import { StateUtil } from './StateUtil';

export interface IStorageService<T> {
    salvar(data: T[]): void;
    carregar(): T[];
}

@Injectable({ providedIn: 'root' })
export abstract class CarrinhoBaseService<T extends Entity<any>> extends Unsubscribe {
    protected readonly state: StateUtil<T>;

    public readonly items$: Observable<T[]>;
    public readonly count$: Observable<number>;

    constructor(private readonly storageService: IStorageService<T>, private readonly storageKey: string) {
        super();

        const itensCarregados = this.storageService.carregar();
        const itensIniciais = itensCarregados || [];
        this.state = new StateUtil<T>(itensIniciais);

        this.items$ = this.state.value$;
        this.count$ = this.items$.pipe(map(items => items.length));

        this.items$
            .pipe(
                takeUntil(this.unsubscribe$),
                tap(items => this.storageService.salvar(items))
            )
            .subscribe();
    }

    public clear(): void {
        this.state.clear();
    }

    public get currentItems(): T[] {
        return this.state.value;
    }

    public get isEmpty(): boolean {
        return this.state.isEmpty;
    }

    public exists(id: T['id']): boolean {
        return !!this.state.findItem(id);
    }
}

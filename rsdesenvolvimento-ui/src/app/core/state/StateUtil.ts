import { BehaviorSubject, Observable } from 'rxjs';
import { Entity } from '../components/models/entity.model';

export class StateUtil<T extends Entity<any>> {
    private readonly state$: BehaviorSubject<T[]>;

    public readonly value$: Observable<T[]>;

    constructor(state: T[] = []) {
        this.state$ = new BehaviorSubject<T[]>(state);
        this.value$ = this.state$.asObservable();
    }

    public get value(): T[] {
        return this.state$.getValue();
    }

    public setState(state: T[]): void {
        this.state$.next(state);
    }

    public get isEmpty(): boolean {
        return this.value.length === 0;
    }

    public addItem(item: T): void {
        const currentState = this.value;
        this.setState([...currentState, item]);
    }

    public updateItem(id: T['id'], partialItem: Partial<T>): void {
        const currentState = this.value;
        const updatedState = currentState.map(item => (item.id === id ? { ...item, ...partialItem } : item));
        this.setState(updatedState);
    }

    public removeItem(id: T['id']): void {
        const currentState = this.value;
        const updatedState = currentState.filter(item => item.id !== id);
        this.setState(updatedState);
    }

    public findItem(id: T['id']): T | undefined {
        return this.value.find(item => item.id === id);
    }

    public clear(): void {
        this.setState([]);
    }

    public destroy(): void {
        this.state$.complete();
    }
}

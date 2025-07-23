import { BehaviorSubject, Observable } from 'rxjs';

export abstract class StateManager<T> {
    private readonly subject: BehaviorSubject<T>;
    public readonly value$: Observable<T>;

    protected constructor(initialValue: T) {
        this.subject = new BehaviorSubject<T>(initialValue);
        this.value$ = this.subject.asObservable();
    }

    public get currentValue(): T {
        return this.subject.value;
    }

    protected setState(value: T): void {
        this.subject.next(value);
    }

    protected updateState(updateFn: (currentState: T) => T): void {
        const newState = updateFn(this.currentValue);
        this.setState(newState);
    }

    public destroy(): void {
        this.subject.complete();
    }
}

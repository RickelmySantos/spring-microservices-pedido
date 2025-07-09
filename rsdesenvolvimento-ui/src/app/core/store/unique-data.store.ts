import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';

@Injectable()
export class UniqueDataStore<T> implements OnDestroy {
    private readonly data = new BehaviorSubject<T>({} as T);
    private readonly data$ = this.data.asObservable();

    private subscription: Subscription;

    constructor() {
        this.subscription = this.data$.subscribe();
    }

    set value(state: T) {
        this.data.next(state);
    }

    get value(): T {
        return this.data.value;
    }

    get value$(): Observable<T> {
        return this.data$;
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}

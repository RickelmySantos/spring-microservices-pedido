import { Directive, OnDestroy } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Directive({ standalone: true })
export abstract class Unsubscribe implements OnDestroy {
    protected readonly unsubscribe$ = new Subject<void>();

    protected get unsubscribeSignal(): Observable<void> {
        return this.unsubscribe$.asObservable();
    }

    ngOnDestroy(): void {
        this.unsubscribe$.next();
        this.unsubscribe$.complete();
    }
}

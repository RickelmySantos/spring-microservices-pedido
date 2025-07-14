import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, shareReplay, take } from 'rxjs';
import { Entity } from 'src/app/core/components/models/entity.model';
import { environments } from 'src/environments/environments';

@Injectable()
export abstract class QueryService<E extends Entity<any>> {
    protected API: string = environments.apiUrl;
    protected abstract PATH: string;
    protected http: HttpClient = inject(HttpClient);

    protected getUrl(): string {
        return `${this.API}/${this.PATH}`;
    }

    list(id: number): Observable<E> {
        return this.http.get<E>(`${this.getUrl()}/${id}`).pipe(shareReplay(1), take(1));
    }

    listAll(): Observable<E[]> {
        return this.http.get<E[]>(`${this.getUrl()}`).pipe(shareReplay(1), take(1));
    }
}

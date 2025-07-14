import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Entity } from 'src/app/core/components/models/entity.model';
import { QueryService } from 'src/app/core/services/query.service';

@Injectable()
export abstract class CrudService<E extends Entity<any>> extends QueryService<E> {
    constructor() {
        super();
    }

    protected override getUrl(): string {
        return `${this.API}/${this.PATH}`;
    }

    save(entidade: E): Observable<E> {
        if (entidade.id) {
            return this.update(entidade.id, entidade);
        } else {
            return this.create(entidade as Omit<E, 'id'>);
        }
    }

    saveAll(entidades: E[]): Observable<E[]> {
        return this.http.post<E[]>(this.getUrl(), entidades);
    }

    private create(entidade: Omit<E, 'id'>): Observable<E> {
        return this.http.post<E>(this.getUrl(), entidade);
    }
    private update(id: E['id'], entidade: E): Observable<E> {
        return this.http.put<E>(`${this.getUrl()}/${id}`, entidade);
    }
}

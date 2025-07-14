import { Entity } from 'src/app/core/components/models/entity.model';

export interface Usuario extends Entity<string> {
    nome: string;
    email: string;
}

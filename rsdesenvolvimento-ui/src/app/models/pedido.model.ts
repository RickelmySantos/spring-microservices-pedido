import { Entity } from 'src/app/core/components/models/entity.model';
import { StatusEnum } from 'src/app/shap-red/enums/statusEnum.enum';

export interface Pedido extends Entity<number> {
    usuarioId: string;
    descricao: string;
    nomeUsuario: string;
    emailUsuario: string;
    status: StatusEnum;
}

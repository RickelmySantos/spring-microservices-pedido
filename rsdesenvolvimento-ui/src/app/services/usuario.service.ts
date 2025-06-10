import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/core/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
    constructor(private oauthService: AuthService) {}

    findbyUser(): any {
        return this.oauthService.getIdentityClaims();
    }
}

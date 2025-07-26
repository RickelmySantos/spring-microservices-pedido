import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';
import { hasPermission, hasRole } from 'src/app/core/auth/auth-utils';
import { mapUser, User } from 'src/app/core/auth/seguranca/models/usuario.model';
import { Permission } from 'src/app/shared/auth/permissions.enum.';
import { Role } from 'src/app/shared/auth/role.enum';

@Injectable({
    providedIn: 'root',
})
export class AuthorizationService {
    private oauthService: OAuthService = inject(OAuthService);
    private _user: User | null = null;

    constructor() {
        this.oauthService.events.pipe(filter(e => e.type === 'token_received' || e.type === 'session_terminated')).subscribe(() => this.loadUserRoles());
        this.loadUserRoles();
    }

    private loadUserRoles(): void {
        if (this.oauthService.hasValidIdToken()) {
            const claims = this.oauthService.getIdentityClaims() as any;

            const realmRoles = claims?.realm_access?.roles || [];

            let clientRoles: string[] = [];
            if (claims?.resource_access) {
                for (const clientName in claims.resource_access) {
                    const resource = claims.resource_access[clientName];
                    if (resource?.roles) {
                        clientRoles = [...clientRoles, ...resource.roles];
                    }
                }
            }

            const allRoles = [...new Set([...realmRoles, ...clientRoles])];

            this._user = mapUser(claims);
            console.log('🔍 DEBUG - Usuário logado:', this._user);
        } else {
            this._user = null;
        }
    }

    getuser(): User | null {
        return this._user;
    }

    hasAnyRole(roles: keyof typeof Role | Role | (keyof typeof Role)[] | Role[]): boolean {
        return hasRole(this._user, roles);
    }

    can(permissions: Permission): boolean {
        return hasPermission(this._user, permissions);
    }

    hasPermissions(permissions: Permission | Permission[]): boolean {
        return hasPermission(this._user, permissions);
    }
}

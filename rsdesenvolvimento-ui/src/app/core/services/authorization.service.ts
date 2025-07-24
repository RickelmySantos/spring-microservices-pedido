import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';

import { Role } from 'src/app/shared/enums/role.enum';

@Injectable({
    providedIn: 'root',
})
export class AuthorizationService {
    private oauthService: OAuthService = inject(OAuthService);
    private _userRoles: string[] = [];

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

            this._userRoles = [...new Set([...realmRoles, ...clientRoles])];
        } else {
            this._userRoles = [];
        }
    }

    getUserRoles(): string[] {
        return this._userRoles;
    }

    hasRole(role: Role | string): boolean {
        return this.getUserRoles().includes(role as string);
    }

    hasAnyRole(roles: keyof typeof Role | Role | (keyof typeof Role)[] | Role[]): boolean {
        if (!roles || (Array.isArray(roles) && roles.length === 0)) {
            return true;
        }
        if (Array.isArray(roles)) {
            return roles.some(role => this.hasRole(role));
        }
        return this.hasRole(roles);
    }

    hasAllRoles(roles: (Role | string)[]): boolean {
        if (!roles || roles.length === 0) return true;
        return roles.every(role => this.getUserRoles().includes(role as string));
    }

    isAdmin(): boolean {
        return this.hasRole(Role.ADMIN);
    }

    isGestor(): boolean {
        return this.hasRole(Role.GESTOR);
    }

    isUsuario(): boolean {
        return this.hasRole(Role.USUARIO);
    }
}

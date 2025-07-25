import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';
import { PERMISSION_MAP } from 'src/app/shared/auth/authorization';
import { Permission } from 'src/app/shared/auth/permissions.enum.';

import { Role } from 'src/app/shared/auth/role.enum';

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

    can(permissions: Permission): boolean {
        const requiredRoles = PERMISSION_MAP[permissions];

        if (!requiredRoles || requiredRoles.length === 0) {
            return false;
        }

        const hasPermission = this.hasAnyRole(requiredRoles);

        return hasPermission;
    }

    public authenticatedUserHasPermissions(permissions: Permission | Permission[]): boolean {
        if (Array.isArray(permissions)) {
            return permissions.every(permission => this.can(permission));
        }
        return this.can(permissions);
    }

    hasPermissions(user: any, permissions: Permission | Permission[]): boolean {
        if (!user || !user.permissions) return false;

        const userPermissions = user.permissions as Permission[];

        if (Array.isArray(permissions)) {
            return permissions.every(permission => userPermissions.includes(permission));
        }

        return userPermissions.includes(permissions);
    }
}

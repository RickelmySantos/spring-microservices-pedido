import { inject, Injectable, InjectionToken } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';
import { authConfig } from 'src/app/core/auth/auth-config';
import { hasPermission, hasRole } from 'src/app/core/auth/auth-utils';
import { mapUser, User } from 'src/app/core/auth/seguranca/models/usuario.model';
import { UniqueDataStore } from 'src/app/core/store/unique-data.store';
import { Permission } from 'src/app/shared/auth/permissions.enum.';
import { Role } from 'src/app/shared/auth/role.enum';

export const AUTH_STORE = new InjectionToken<UniqueDataStore<User>>('AuthStore', { providedIn: 'root', factory: () => new UniqueDataStore<User>() });

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly oauthService: OAuthService = inject(OAuthService);
    private readonly userStore: UniqueDataStore<User> = inject(AUTH_STORE);
    private userProfile: User;

    constructor() {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        this.oauthService.disablePKCE = false;

        this.oauthService.events.pipe(filter(e => e.type === 'token_received' || e.type === 'session_terminated')).subscribe(() => this.loadUserRoles());
        this.loadUserRoles();
    }

    async initAuth(): Promise<void> {
        try {
            await this.oauthService.loadDiscoveryDocumentAndTryLogin();
            if (!this.hasValidToken()) {
                this.login();
            } else {
                console.debug('[AuthService] Token válido detectado.');
            }
        } catch (e) {
            this.login();
        }
    }

    login(): void {
        this.oauthService.initLoginFlow();
    }
    logout(): void {
        this.oauthService.revokeTokenAndLogout();
    }

    isLoggedIn(): boolean {
        return this.hasValidToken();
    }

    getAccessToken(): string | null {
        return this.oauthService.getAccessToken();
    }

    getIdentityClaims(): any {
        return this.oauthService.getIdentityClaims();
    }

    get user(): User {
        return this.userStore.value;
    }

    get user$() {
        return this.userStore.value$;
    }

    loadUserInfo(): Promise<User> {
        if (this.hasValidToken()) {
            const claims = this.getIdentityClaims();

            const user: User = mapUser(claims);

            this.userProfile = user;

            return Promise.resolve(user);
        }
        return Promise.resolve(this.userProfile || null);
    }
    private loadUserRoles(): void {
        if (this.oauthService.hasValidIdToken()) {
            const claims = this.oauthService.getIdentityClaims() as any;

            let clientRoles: string[] = [];
            if (claims?.resource_access) {
                for (const clientName in claims.resource_access) {
                    const resource = claims.resource_access[clientName];
                    if (resource?.roles) {
                        clientRoles = [...clientRoles, ...resource.roles];
                    }
                }
            }

            this.userProfile = mapUser(claims);
            console.log('🔍 DEBUG - Usuário logado:', this.userProfile);
        } else {
            this.userProfile = null;
        }
    }

    private hasValidToken(): boolean {
        return this.oauthService.hasValidIdToken() && this.oauthService.hasValidAccessToken();
    }

    hasPermissions(permissions: Permission | Permission[]): boolean {
        return hasPermission(this.userProfile, permissions);
    }

    hasAnyRole(roles: keyof typeof Role | Role | (keyof typeof Role)[] | Role[]): boolean {
        return hasRole(this.userProfile, roles);
    }
}

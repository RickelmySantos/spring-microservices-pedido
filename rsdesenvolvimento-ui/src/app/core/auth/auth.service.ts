import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from 'src/app/core/auth/auth-config';
import { mapUser, User } from 'src/app/core/auth/seguranca/models/usuario.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly oauthService: OAuthService = inject(OAuthService);

    private userProfile: User;

    constructor() {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        this.oauthService.disablePKCE = false;
    }

    async initAuth(): Promise<void> {
        try {
            await this.oauthService.loadDiscoveryDocumentAndTryLogin();
            if (!this.hasValidToken()) {
                this.login();
            } else {
                console.debug('Login silencioso bem-sucedido. Token válido encontrado.');
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

    private hasValidToken(): boolean {
        return this.oauthService.hasValidIdToken() && this.oauthService.hasValidAccessToken();
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
}

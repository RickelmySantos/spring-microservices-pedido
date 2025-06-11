import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from 'src/app/core/auth/auth-config';

@Injectable()
export class AuthService {
    private readonly oauthService: OAuthService = inject(OAuthService);

    constructor() {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        this.oauthService.disablePKCE = false;
    }

    async initAuth(): Promise<void> {
        try {
            await this.oauthService.loadDiscoveryDocument();
            await this.oauthService.tryLoginCodeFlow();

            if (this.hasValidToken()) {
                console.debug('Token de acesso válido:', this.oauthService.getAccessToken());
                return;
            }

            this.login();
        } catch (error) {
            console.error('Erro ao inicializar autenticação:', error);
            this.oauthService.logOut();
            this.login();
        }
    }

    public hasValidToken(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    login(): void {
        this.oauthService.initLoginFlow();
    }
    logout(): void {
        this.oauthService.revokeTokenAndLogout();
        this.oauthService.logOut();
    }

    isLoggedIn(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    getAccessToken(): string | null {
        return this.oauthService.getAccessToken();
    }

    getIdentityClaims(): any {
        return this.oauthService.getIdentityClaims();
    }

    getProfile() {
        return this.oauthService.getIdentityClaims();
    }

    getToken() {
        return this.oauthService.getAccessToken();
    }

    // private hasValidAccessToken(): boolean {
    //     return this.oauthService.hasValidAccessToken() && this.oauthService.hasValidIdToken();
    // }

    // private async loadToken(): Promise<boolean> {
    //     return await this.oauthService.loadDiscoveryDocumentAndTryLogin({} as LoginOptions).catch(err => {
    //         return this.oauthService.loadDiscoveryDocumentAndLogin();
    //     });
    // }
}

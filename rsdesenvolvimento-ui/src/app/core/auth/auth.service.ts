import { inject, Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from 'src/app/core/auth/auth-config';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly oauthService: OAuthService = inject(OAuthService);

    constructor() {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        this.oauthService.disablePKCE = false;

        // this.runInitialLoginSequence();
    }

    async initAuth(): Promise<void> {
        try {
            await this.oauthService.loadDiscoveryDocumentAndTryLogin();
            if (!this.oauthService.hasValidAccessToken()) {
                this.oauthService.initLoginFlow();
            } else {
                console.debug('Login silencioso bem-sucedido. Token válido encontrado.');
            }
        } catch (e) {
            console.error('Erro ao carregar discovery document ou tentar login:', e);
            if (e instanceof Error && 'details' in e && typeof (e as any).details === 'string') {
                console.log('Detalhes do erro:', e.details);
            }
            this.oauthService.initLoginFlow();
        }
    }
    private async runInitialLoginSequence(): Promise<void> {
        try {
            await this.oauthService.loadDiscoveryDocumentAndTryLogin();
            if (this.oauthService.hasValidAccessToken()) {
                console.debug('Login silencioso bem-sucedido.');
            }
        } catch (error) {
            console.error('Erro durante a tentativa de login silencioso.', error);
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
}

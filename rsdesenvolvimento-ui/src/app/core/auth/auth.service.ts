import { inject, Injectable } from '@angular/core';
import { LoginOptions, OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from 'src/app/core/auth/auth-config';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly oauthService: OAuthService = inject(OAuthService);

    constructor() {
        this.oauthService.configure(authConfig);
        this.oauthService.setupAutomaticSilentRefresh();

        this.oauthService.disablePKCE = false;
    }

    async initializeAuthentication(): Promise<void> {
        await this.oauthService.loadDiscoveryDocument();

        const hasValidToken = await this.oauthService.tryLogin();

        if (!hasValidToken || !this.oauthService.hasValidAccessToken()) {
            this.oauthService.initLoginFlow();
        }
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

    private hasValidAccessToken(): boolean {
        return this.oauthService.hasValidAccessToken() && this.oauthService.hasValidIdToken();
    }

    private async loadToken(): Promise<boolean> {
        return await this.oauthService.loadDiscoveryDocumentAndTryLogin({} as LoginOptions).catch(err => {
            return this.oauthService.loadDiscoveryDocumentAndLogin();
        });
    }
}

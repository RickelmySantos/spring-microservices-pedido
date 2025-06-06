import { AuthConfig } from 'angular-oauth2-oidc';
import { authProperties } from 'src/environments/auth.properties';

export const authConfig: AuthConfig = {
    issuer: authProperties['issuer'],
    clientId: authProperties['clientId'],
    responseType: authProperties['responseType'],
    redirectUri: authProperties['redirectUri'],
    strictDiscoveryDocumentValidation: authProperties['strictDiscoveryDocumentValidation'],
    scope: authProperties['scope'],
    requireHttps: authProperties['requireHttps'],

    sessionChecksEnabled: true,
    sessionCheckIntervall: 30000,
    preserveRequestedRoute: true,
    showDebugInformation: true,

    // silentRefreshTimeout: 5000,
};

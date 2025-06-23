import { AuthConfig } from 'angular-oauth2-oidc';

export const authProperties: AuthConfig = {
    issuer: 'http://localhost:8086/auth/realms/REALM_LOCAL',
    clientId: 'rsdesenvolvimento-ui',
    redirectUri: window.location.origin,
    responseType: 'code',

    strictDiscoveryDocumentValidation: true,
    scope: 'openid profile email roles ',
    requireHttps: false,
    showDebugInformation: true,
};

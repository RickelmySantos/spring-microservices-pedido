import { AuthConfig } from 'angular-oauth2-oidc';

console.log('1. Lendo auth.config.ts. Valor de window.env:', window['env']);

const rawIssuer = window['env']?.['OAUTH_ISSUER'];
const issuer = typeof rawIssuer === 'string' && rawIssuer.trim() !== '' && !rawIssuer.includes('${') ? rawIssuer : 'http://localhost:8086/auth/realms/REALM_LOCAL';

export const authProperties: AuthConfig = {
    issuer,
    clientId: window['env']?.['OAUTH_CLIENT_ID'] || 'rsdesenvolvimento-ui',
    redirectUri: window.location.origin,
    responseType: 'code',
    strictDiscoveryDocumentValidation: true,
    scope: 'openid profile email roles',
    requireHttps: window['env']?.['REQUIRE_HTTPS'] === 'true',
    showDebugInformation: window['env']?.['LOGGING_LEVEL'] === 'debug',
};

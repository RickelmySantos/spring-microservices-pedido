import { AuthConfig } from 'angular-oauth2-oidc';

export const authProperties: AuthConfig = {
    issuer: window['env']['OAUTH_ISSUER'] !== '${OAUTH_ISSUER}' ? window['env']['OAUTH_ISSUER'] : 'http://localhost:8086/auth/realms/REALM_LOCAL',
    clientId: window['env']['OAUTH_CLIENT_ID'] !== '${OAUTH_CLIENT_ID}' ? window['env']['OAUTH_CLIENT_ID'] : 'rsdesenvolvimento-ui',
    redirectUri: window.location.origin,
    responseType: 'code',
    strictDiscoveryDocumentValidation: true,
    scope: 'openid profile email roles',
    requireHttps: window['env']['REQUIRE_HTTPS'] === 'true',
    showDebugInformation: window['env']['LOGGING_LEVEL'] === 'debug',
};

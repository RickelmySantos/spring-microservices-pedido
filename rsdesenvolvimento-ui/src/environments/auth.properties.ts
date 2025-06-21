import { AuthConfig } from 'angular-oauth2-oidc';

export const authProperties: AuthConfig = {
    issuer: 'http://localhost:8086/auth/realms/REALM_LOCAL',
    clientId: window['env']['OAUTH_CLIENT_ID'] !== '${OAUTH_CLIENT_ID}' ? window['env']['OAUTH_CLIENT_ID'] : 'rsdesenvolvimento-ui',
    redirectUri: window.location.origin,
    responseType: 'code',

    strictDiscoveryDocumentValidation: true,
    scope: 'openid profile email roles ',
    requireHttps: window['env']['REQUIRE_HTTPS'] === 'true',
    showDebugInformation: window['env']['LOGGING_LEVEL'] === 'debug' ? true : false,
};

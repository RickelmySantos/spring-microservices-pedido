(function (window) {
    window['env'] = window['env'] || {};

    // Environment variables
    window['env']['PRODUCTION'] = true;
    window['env']['IMAGE_VERSION'] = '1.0.0';
    window['env']['AMBIENTE'] = 'Local';
    window['env']['BASE_URL'] = 'http://localhost:8080';
    window['env']['DEFAULT_TIMEZONE'] = 'America/Belem';
    window['env']['DEFAULT_LOCALE'] = 'pt-BR';

    window['env']['OAUTH_ISSUER'] = 'http://keycloak:8080/auth/realms/REALM_LOCAL';
    window['env']['OAUTH_CLIENT_ID'] = 'rsdesenvolvimento-ui';

    window['env']['REQUIRE_HTTPS'] = 'false';
})(this);

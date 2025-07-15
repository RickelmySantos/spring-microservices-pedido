(function (window) {
    window['env'] = window['env'] || {};
    // window['env']['PRODUCTION'] = '${PRODUCTION}';
    window['env']['BASE_URL'] = '${BASE_URL}';
    window['env']['UI_ROOT'] = '${UI_ROOT}';
    window['env']['API_URL'] = '${API_URL}';
    // window['env']['AMBIENTE'] = '${AMBIENTE}';
    window['env']['DEFAULT_LOCALE'] = '${DEFAULT_LOCALE}';
    window['env']['DEFAULT_TIMEZONE'] = '${DEFAULT_TIMEZONE}';
    // window['env']['IMAGE_VERSION'] = '${IMAGE_VERSION}';
    window['env']['OAUTH_CLIENT_ID'] = '${OAUTH_CLIENT_ID}';
    window['env']['OAUTH_ISSUER'] = '${OAUTH_ISSUER}';
    window['env']['REQUIRE_HTTPS'] = '${REQUIRE_HTTPS}';
})(this);

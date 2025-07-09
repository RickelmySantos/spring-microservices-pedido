#!/bin/sh

echo "[Entrypoint] Substituindo variáveis de ambiente no env.js..."

export $(env | cut -d= -f1)

echo "[Entrypoint] Variáveis disponíveis:"
env

envsubst '${BASE_URL} ${UI_ROOT} ${API_URL} ${DEFAULT_LOCALE} ${DEFAULT_TIMEZONE} ${OAUTH_CLIENT_ID} ${OAUTH_ISSUER} ${REQUIRE_HTTPS}' \
  < /usr/share/nginx/html/assets/env.template.js \
  > /usr/share/nginx/html/assets/env.js

echo "[Entrypoint] Conteúdo final de env.js:"
cat /usr/share/nginx/html/assets/env.js

exec nginx -g 'daemon off;'

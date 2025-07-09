#!/bin/sh

echo "[Entrypoint] Substituindo variáveis de ambiente no env.js..."

# Exporta todas as variáveis de ambiente para garantir que o envsubst as reconheça
export $(env | cut -d= -f1)

# Lista as variáveis disponíveis (debug)
echo "[Entrypoint] Variáveis disponíveis:"
env

# Substituição explícita das variáveis esperadas
envsubst '${BASE_URL} ${AMBIENTE} ${DEFAULT_LOCALE} ${DEFAULT_TIMEZONE} ${DOCS_URL} ${IMAGE_VERSION} ${OAUTH_CLIENT_ID} ${OAUTH_ISSUER} ${REQUIRE_HTTPS}' \
  < /usr/share/nginx/html/assets/env.template.js \
  > /usr/share/nginx/html/assets/env.js

# Mostra o conteúdo final do env.js para depuração
echo "[Entrypoint] Conteúdo final de env.js:"
cat /usr/share/nginx/html/assets/env.js

# Inicia o Nginx
exec nginx -g 'daemon off;'

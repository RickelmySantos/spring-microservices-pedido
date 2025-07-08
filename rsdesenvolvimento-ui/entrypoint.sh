#!/bin/sh
# Pega o template, substitui as variáveis de ambiente e cria o env.js final
envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js

# Inicia o Nginx para servir a aplicação
exec nginx -g 'daemon off;'
#!/bin/bash

# if [[ $1 == "reset" ]]; then
#     docker compose down
#     echo "Recriando a infraestrutura local..."
#     sudo rm -rf ./.app
#     docker compose up -d --force-recreate
# elif [[ $1 == "start" ]]; then
#     echo "Inicializando a infraestrutura local..."
#     docker compose up -d
# elif [[ $1 == "stop" ]]; then
#     echo "Interrompendo a execução da infraestrutura local..."
#     docker compose stop
# fi


#!/bin/bash

# fail fast
set -e

if [[ $1 == "reset" ]]; then
    echo "🔄 Resetando infraestrutura completa..."

    # Desce todos os containers
    docker compose down --volumes --remove-orphans

    # Remove as imagens construídas (apenas as imagens locais do projeto, não as imagens de repositórios externos como postgres, keycloak, etc)
    echo "🗑️  Limpando imagens buildadas localmente..."
    docker images --filter=reference='spring-microservices-pedido_*' --format '{{.Repository}}:{{.Tag}}' | xargs -r docker rmi

    echo "✅ Infraestrutura resetada."

    # Subindo de novo
    echo "🚀 Subindo infraestrutura novamente..."
    docker compose up -d --build --force-recreate

elif [[ $1 == "start" ]]; then
    echo "🚀 Inicializando a infraestrutura local..."
    docker compose up -d --build

elif [[ $1 == "stop" ]]; then
    echo "🛑 Interrompendo a execução da infraestrutura local..."
    docker compose stop

else
    echo "❓ Uso: $0 {reset|start|stop}"
    exit 1
fi

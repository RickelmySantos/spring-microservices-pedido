#!/bin/bash

if [[ $1 == "reset" ]]; then
    docker compose down
    echo "Recriando a infraestrutura local..."
    sudo rm -rf ./.app
    docker compose up -d --force-recreate
elif [[ $1 == "start" ]]; then
    echo "Inicializando a infraestrutura local..."
    docker compose up -d
elif [[ $1 == "stop" ]]; then
    echo "Interrompendo a execução da infraestrutura local..."
    docker compose stop
fi


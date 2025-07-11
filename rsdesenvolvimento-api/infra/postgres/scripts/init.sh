#!/bin/bash
set -e

echo "[INIT] Aguardando PostgreSQL iniciar..."

until pg_isready -U "$POSTGRES_USER" > /dev/null 2>&1; do
    sleep 1
done

echo "[INIT] PostgreSQL está pronto."

echo "[INIT] Criando bancos de dados..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE usuario;
    CREATE DATABASE pedido;
    CREATE DATABASE pagamento;
    CREATE DATABASE notificacao;
    CREATE DATABASE estoque;
    CREATE DATABASE keycloak;
EOSQL

echo "[INIT] Bancos de dados criados com sucesso."

echo "[INIT] Criando schema no banco keycloak..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname=keycloak <<-EOSQL
    CREATE SCHEMA IF NOT EXISTS keycloak;
EOSQL

echo "[INIT] Inicialização finalizada com sucesso..."

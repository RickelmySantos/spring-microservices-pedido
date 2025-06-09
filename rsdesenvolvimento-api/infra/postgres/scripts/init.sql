CREATE DATABASE usuario;
CREATE DATABASE pedido;
CREATE DATABASE pagamento;
CREATE DATABASE notificacao;
CREATE DATABASE estoque;
CREATE DATABASE keycloak;

-- conecta no banco keycloak e cria o schema
\connect keycloak;

CREATE SCHEMA IF NOT EXISTS keycloak;
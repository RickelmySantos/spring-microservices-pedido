CREATE SEQUENCE IF NOT EXISTS pedido_seq START 1;

CREATE TABLE IF NOT EXISTS pedido (
    id BIGINT PRIMARY KEY DEFAULT nextval('pedido_seq'),
    descricao VARCHAR(255),
    usuario_id BIGINT NOT NULL,
    data_hora_criacao TIMESTAMP,
    criado_por VARCHAR(255),
    data_hora_atualizacao TIMESTAMP,
    atualizado_por VARCHAR(255)
);

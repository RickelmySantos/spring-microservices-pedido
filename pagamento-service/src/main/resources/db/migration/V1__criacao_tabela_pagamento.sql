CREATE TABLE pagamento (
    id SERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

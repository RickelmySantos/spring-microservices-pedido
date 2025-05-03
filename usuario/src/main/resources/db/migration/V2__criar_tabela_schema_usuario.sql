CREATE TABLE usuario.usuario (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL,
    data_hora_criacao TIMESTAMP,
    criado_por VARCHAR(255),
    data_hora_atualizacao TIMESTAMP,
    atualizado_por VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS produto (
    id BIGINT NOTNULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    estoque INT DEFAULT 0,
    imagem_url VARCHAR(255),
    data_hora_criacao TIMESTAMP,
    criado_por VARCHAR(255),
    data_hora_atualizacao TIMESTAMP,
    atualizado_por VARCHAR(255)
);

SET SCHEMA 'estoque';

INSERT INTO TB_ESTOQUE
    (NOME, DESCRICAO, PRECO, CATEGORIA, ESTOQUE, IMAGEM_URL, DATA_HORA_CRIACAO, CRIADO_POR, DATA_HORA_ATUALIZACAO, ATUALIZADO_POR)
VALUES
(1, 'Salpicão de Frango Tradicional', 'O Salpicão de Frango é um prato tradicional', 25.00, 'Prato Principal', 10, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749556914/rweqrdqc9ply1ibk2tfz.png', '2025-06-10 12:01:55.2086', 'ESTOQUE_MS'),
(2, 'Strogonoff de Carne com Creme de Leite Nestlé e Caldo Maggi', 'Receita de Strogonoff de carne', 40.00, 'Prato Principal', 8, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749557054/frazv2bvij4mgscjxtaa.png', '2025-06-10 12:04:15.2002', 'ESTOQUE_MS'),
(3, 'Strogonoff de Frango', 'Receita de Strogonoff de frango', 40.00, 'Prato Principal', 10, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749557194/i9ae8jwpg5m8grjfoqpq.png', '2025-06-10 12:06:34.5954', 'ESTOQUE_MS'),
(4, 'Salmão Grelhado com Molho de Vodka e Amêndoas', 'Receita de Salmão Grelhado', 60.00, 'Prato Principal', 5, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749557328/ki7mqwze5ux1vye10pxs.png', '2025-06-10 12:08:48.7231', 'ESTOQUE_MS'),
(5, 'Mocotó', 'O Mocotó é uma receita típica brasileira', 30.00, 'Prato Principal', 10, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749557927/rbyscd99b9seitmjslrs.png', '2025-06-10 12:18:47.9888', 'ESTOQUE_MS'),
(6, 'Gelatina de Café com Leite MOÇA', 'Gelatina de Café com Leite MOÇA', 15.00, 'Sobremesa', 20, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749558067/onkcy6qpdpeqwwkz8whv.png', '2025-06-10 12:21:08.3797', 'ESTOQUE_MS'),
(7, 'Mousse de Maracujá com a Fruta', 'Mousse de Maracujá simples e refrescante', 15.00, 'Sobremesa', 30, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749558198/jalcaxsyjcvxdk6ngi7x.png', '2025-06-10 12:23:19.6494', 'ESTOQUE_MS'),
(9, 'Pudim de Leite Condensado', 'Pudim de Leite Condensado com Calda de Caramelo', 20.00, 'Sobremesa', 25, 'https://res.cloudinary.com/rsdesenvolvimento-estoque-api/image/upload/v1749558280/jipi9ml9dzwbnrhwpxod.png', '2025-06-10 12:25:30.9191', 'ESTOQUE_MS'),

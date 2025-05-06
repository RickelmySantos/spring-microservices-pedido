ALTER TABLE pedido
ADD COLUMN nome_usuario VARCHAR(255);

ALTER TABLE pedido
ADD COLUMN email_usuario VARCHAR(255);

ALTER TABLE pedido
ADD COLUMN status_pedido VARCHAR(255);
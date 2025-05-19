ALTER TABLE pagamento ADD COLUMN data_hora_criacao TIMESTAMP;
ALTER TABLE pagamento ADD COLUMN criado_por VARCHAR(255);
ALTER TABLE pagamento ADD COLUMN data_hora_atualizacao TIMESTAMP;
ALTER TABLE pagamento ADD COLUMN atualizado_por VARCHAR(255);

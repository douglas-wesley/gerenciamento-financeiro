CREATE TABLE tb_conta (
   id BIGSERIAL PRIMARY KEY,
   nome_banco VARCHAR(255),
   nome_titular VARCHAR(255),
   numero_conta INTEGER,
   email VARCHAR(255) UNIQUE NOT NULL,
   senha VARCHAR(255) NOT NULL,
   saldo NUMERIC(15,2)
);

-- Criação da tabela de categorias
CREATE TABLE tb_categoria (
   id BIGSERIAL PRIMARY KEY,
   nome_categoria VARCHAR(255) NOT NULL,
   cor VARCHAR(50) -- ou SMALLINT/INT, se seu enum for numeric
);

-- Criação da tabela de transações
CREATE TABLE tb_transacao (
   id BIGSERIAL PRIMARY KEY,
   descricao VARCHAR(255),
   valor NUMERIC(15,2) NOT NULL,
   data DATE,
   tipo VARCHAR(50) NOT NULL,
   conta_id BIGINT,
   CONSTRAINT fk_transacao_conta
   FOREIGN KEY (conta_id) REFERENCES tb_conta(id)
);

-- Tabela de relacionamento N:N entre transação e categoria
CREATE TABLE transacao_categoria (
  transacao_id BIGINT NOT NULL,
  categoria_id BIGINT NOT NULL,
  PRIMARY KEY (transacao_id, categoria_id),
  CONSTRAINT fk_tc_transacao
  FOREIGN KEY (transacao_id) REFERENCES tb_transacao(id),
  CONSTRAINT fk_tc_categoria
  FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id)
);
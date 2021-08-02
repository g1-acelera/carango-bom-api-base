-- Table: Veículo
CREATE TABLE veiculo
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    nome character varying(255) NOT NULL,
	ano int NOT NULL,
	valor float8 NOT NULL,
	marca_Id bigint NOT NULL,
    CONSTRAINT veiculo_pkey PRIMARY KEY (id),
	CONSTRAINT veiculo_marca_fkey FOREIGN KEY(marcaId) REFERENCES marca(id)
);

-- Table: Autenticação
CREATE TABLE autenticacao
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    email character varying(100) NOT NULL,
	senha character varying(200) NOT NULL,
    CONSTRAINT autenticacao_pkey PRIMARY KEY (id)
);

-- Insert Table: Autenticação
-- Senha original: teste

INSERT INTO autenticacao (email, senha)
VALUES('admin@email.com', '$2a$10$c8Q0g/hRiuxMFnm30rqr.O2D5HCI54AZkkMA1L3WsR4QjmtMZoix.');

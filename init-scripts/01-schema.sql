CREATE TABLE IF NOT EXISTS endereco (
    id INT AUTO_INCREMENT,
    rua VARCHAR(255) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cep VARCHAR(20) NOT NULL,
    CONSTRAINT pk_endereco PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS tipo_usuario (
    id INT,
    descricao VARCHAR(50) NOT NULL,
    CONSTRAINT pk_tipo_usuario PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    login VARCHAR(150) NOT NULL,
    data_ultima_alteracao DATETIME NULL,
    endereco_id INT NOT NULL,
    tipo_usuario_id INT NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_email UNIQUE (email),
    CONSTRAINT uq_usuario_login UNIQUE (login),
    CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id),
    CONSTRAINT fk_usuario_tipo FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
    );

DROP DATABASE IF EXISTS crud_manager;

CREATE DATABASE IF NOT EXISTS crud_manager;

USE crud_manager;

-- A coluna password guarda o hash PBKDF2 (veja model.PasswordUtil), nunca a senha pura
CREATE TABLE IF NOT EXISTS users(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    sexo ENUM('M', 'F'),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_date DATE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(user_id) 
    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS companies (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(128) NOT NULL,
	`role` VARCHAR(128) NOT NULL,
	`start` DATE NOT NULL,
	`end` DATE,
	user_id INT NOT NULL,
	FOREIGN KEY(user_id)
	REFERENCES users(id)
);

-- NOVO CRUD 1: Lotes de café (talhões)
-- Campos: nome (text), variedade (select), área (number), data de plantio (date),
--         irrigado (checkbox) e proprietário (select -> users)
CREATE TABLE IF NOT EXISTS coffee_lots (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(100) NOT NULL,
	variety VARCHAR(30) NOT NULL,
	area DECIMAL(8,2) NOT NULL,
	planting_date DATE NOT NULL,
	irrigated BOOLEAN NOT NULL DEFAULT FALSE,
	user_id INT NOT NULL,
	FOREIGN KEY(user_id)
	REFERENCES users(id)
);

-- NOVO CRUD 2: Colheitas
-- Campos: lote (select -> coffee_lots), data (date), sacas (number),
--         qualidade (radio), observações (textarea) e responsável (select -> users)
CREATE TABLE IF NOT EXISTS harvests (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	coffee_lot_id INT NOT NULL,
	harvest_date DATE NOT NULL,
	bags INT NOT NULL,
	quality ENUM('ESPECIAL', 'SUPERIOR', 'TRADICIONAL') NOT NULL,
	notes TEXT,
	user_id INT NOT NULL,
	FOREIGN KEY(coffee_lot_id)
	REFERENCES coffee_lots(id),
	FOREIGN KEY(user_id)
	REFERENCES users(id)
);

-- Senha de todos os usuários de exemplo: 123456
INSERT INTO users VALUES
(DEFAULT, "Emerson Carvalho", "M", "emerson@mail.com", "65000:SPtryvgSgVAacxjGTLDDhw==:82AIV53yCMPESdCc+bfolU+GgzVkeGPmPk73krcn6Pc="),
(DEFAULT, "Luiza Carvalho", "F", "lu@mail.com", "65000:VXNYX7UScRbiukrY+jDCuA==:fVXKdt3/0eDXHDhJ0cmmYte93OqK75Z5KHco4n998Oc="),
(DEFAULT, "Elenice Carvalho", "F", "le@mail.com", "65000:SJ9HjdEFLbaFAa2H5YUjeQ==:i3MipQNyK2XZ+PDmGc37AamReXKnaQrMgWpOmMTnd1M="),
(DEFAULT, "Noé Carvalho", "M", "noe@mail.com", "65000:lmOeolKclKUWbVZgeZL0jA==:JMn5jeehO/AbOMVoWiAXxHbO3eBDyohYrdEUNaKnbSw="),
(DEFAULT, "Rosânia Carvalho", "F", "ro@mail.com", "65000:RPHYOFKlewRgld5K+69SYw==:8JBxGv+9BIrpPkH1ZMlcnlzZ/2vk1ikjLJXi1Sr5/Ls=");

INSERT INTO posts VALUES
(DEFAULT, "Olá do Emerson", CURDATE(), 1),
(DEFAULT, "Olá da Luiza", CURDATE(), 2),
(DEFAULT, "Olá da Denise", CURDATE(), 3),
(DEFAULT, "Olá do Noé", CURDATE(), 4),
(DEFAULT, "Olá da Rosânia", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 1", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 2", CURDATE(), 5),
(DEFAULT, "Olá da Rosânia 3", CURDATE(), 5);

INSERT INTO coffee_lots VALUES
(DEFAULT, "Talhão da Nascente", "Catuaí", 4.50, "2015-10-20", TRUE, 1),
(DEFAULT, "Talhão do Morro", "Mundo Novo", 7.25, "2012-11-05", FALSE, 1),
(DEFAULT, "Baixada do Córrego", "Bourbon", 3.00, "2018-02-14", TRUE, 2),
(DEFAULT, "Lote Alto", "Arara", 5.80, "2020-12-01", TRUE, 3),
(DEFAULT, "Pé de Serra", "Acaiá", 2.40, "2010-03-18", FALSE, 4),
(DEFAULT, "Lote da Sede", "Catuaí", 1.75, "2019-01-22", FALSE, 5);

INSERT INTO harvests VALUES
(DEFAULT, 1, "2026-06-10", 120, "ESPECIAL", "Maturação uniforme, secagem em terreiro.", 1),
(DEFAULT, 2, "2026-06-18", 210, "SUPERIOR", "Colheita mecanizada.", 1),
(DEFAULT, 3, "2026-06-25", 65, "ESPECIAL", NULL, 2),
(DEFAULT, 4, "2026-07-02", 140, "SUPERIOR", "Chuva atrasou a secagem em um dia.", 3),
(DEFAULT, 5, "2026-07-09", 48, "TRADICIONAL", NULL, 4),
(DEFAULT, 6, "2026-07-15", 30, "SUPERIOR", "Primeira safra do lote após poda.", 5),
(DEFAULT, 1, "2026-07-20", 35, "TRADICIONAL", "Repasse de colheita.", 2);

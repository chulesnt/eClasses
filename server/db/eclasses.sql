SET time_zone = "-03:00";


CREATE DATABASE IF NOT EXISTS eclasses CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eclasses;


-- Table UF

DROP TABLE IF EXISTS `UF`;
CREATE TABLE IF NOT EXISTS `UF`
(
  "cod-uf" Integer NOT NULL,
  "nome" Character varying(40) NOT NULL,
  "abreviacao" Character varying(2) NOT NULL
);

-- Table município

DROP TABLE IF EXISTS `municipio`;
CREATE TABLE IF NOT EXISTS `municipio`
(
  "cod-município" Integer NOT NULL,
  "nome" Character varying(50) NOT NULL,
  "cod-uf" Integer NOT NULL
);


DROP TABLE IF EXISTS `aluno`;
CREATE TABLE IF NOT EXISTS `aluno`
(
  "id-aluno" Bigint NOT NULL AUTO_INCREMENT,
  "email-aluno" Character varying(255) NOT NULL UNIQUE,
  "senha" Character varying(255) NOT NULL,
  "nome" Character varying(255) NOT NULL,
  "cod-município" Integer NOT NULL,
  "cod-uf" Integer NOT NULL,
  "preferencia-preco" Numeric(6,2),
  "cod-preferencia-local" Integer,
  "preferencia-numero-alunos" Integer,
  "assinante" Boolean NOT NULL,
  "data-fim-assinatura" Date
);





DROP TABLE IF EXISTS `professor`;
CREATE TABLE IF NOT EXISTS `professor`
(
  "id-prof" Bigint NOT NULL AUTO_INCREMENT,
  "email-prof" Character varying(255) NOT NULL UNIQUE,
  "senha" Character varying(255) NOT NULL,
  "nome" Character varying(255) NOT NULL,
  "descricao_apresentacao" Character varying(1023) NOT NULL,
  "titulo_apresentacao" Character varying(50) NOT NULL,
  "premium" Boolean NOT NULL,
  "avaliacao" Numeric(6,4) NOT NULL,
  "numero-avaliacoes" Integer NOT NULL,
  "cod-município" Integer NOT NULL,
  "cod-uf" Integer NOT NULL,
  "preco-hora" Numeric(6,2) NOT NULL,
  "cod-materia" Integer NOT NULL,
  "numero-alunos-min" Integer,
  "numero-alunos-max" Integer,
  "data-fim-premium" Date
);


-- Table preferencia-localizacao

DROP TABLE IF EXISTS `preferencia-localizacao`;
CREATE TABLE IF NOT EXISTS `preferencia-localizacao`
(
  "cod-preferencia-local" Integer NOT NULL,
  "descricao" Character varying(50) NOT NULL
);

-- Table materias

DROP TABLE IF EXISTS `materias`;
CREATE TABLE IF NOT EXISTS `materias`
(
  "cod-materia" Integer NOT NULL AUTO_INCREMENT,
  "nome" Character varying(50) NOT NULL
);


-- Table aluno-preferencias-materias

DROP TABLE IF EXISTS `aluno-preferencias-materias`;
CREATE TABLE IF NOT EXISTS `aluno-preferencias-materias`;
(
  "id-aluno" Bigint NOT NULL,
  "cod-materia" Integer NOT NULL
);


-- Table admin

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin`
(
  "id-admin" Bigint NOT NULL AUTO_INCREMENT,
  "usuario" Character varying(30) NOT NULL,
  "senha" Character varying(255) NOT NULL
);


-- Table agenda-aluno

DROP TABLE IF EXISTS `agenda-aluno`;
CREATE TABLE IF NOT EXISTS `agenda-aluno`
(
  "dia" Date NOT NULL,
  "texto" Character varying(511) NOT NULL,
  "id-aluno" Bigint NOT NULL
);


-- Table agenda-prof

DROP TABLE IF EXISTS `agenda-prof`;
CREATE TABLE IF NOT EXISTS `agenda-prof`
(
  "dia" Date NOT NULL,
  "texto" Character varying(511) NOT NULL,
  "id-prof" Bigint NOT NULL
);


-- Table mensagem
DROP TABLE IF EXISTS `mensagem`;
CREATE TABLE IF NOT EXISTS `mensagem`
(
  "cod-mensagem" Bigint NOT NULL AUTO_INCREMENT,
  "texto" Character varying(255) NOT NULL,
  "id-aluno" Bigint NOT NULL,
  "id-prof" Bigint NOT NULL,
  "aluno-enviou" Boolean NOT NULL,
  "comentario" Boolean NOT NULL,
  "data-horario" Timestamp NOT NULL
);






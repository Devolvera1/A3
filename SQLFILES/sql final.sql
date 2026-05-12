-- ======================================================
-- 1. CONFIGURAÇÃO INICIAL E CRIAÇÃO DO BANCO
-- ======================================================
CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

SET FOREIGN_KEY_CHECKS = 0;

-- Limpeza de tabelas para evitar conflitos
DROP TABLE IF EXISTS registroPonto;
DROP TABLE IF EXISTS historIcoSalarios;
DROP TABLE IF EXISTS despesas;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS funcionarios;
DROP TABLE IF EXISTS cargos;
DROP TABLE IF EXISTS departamentos;

SET FOREIGN_KEY_CHECKS = 1;

-- ======================================================
-- 2. CRIAÇÃO DAS TABELAS
-- ======================================================

CREATE TABLE departamentos (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NOME VARCHAR(250) NOT NULL,
    descricao TEXT NOT NULL
);

CREATE TABLE cargos (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    descricao TEXT NOT NULL,
    salarioBase DECIMAL(10, 2),
    nivel VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR (20) NOT NULL,
    dataNascimento DATE,
    data_admissao DATE NOT NULL,
    status ENUM('ATIVO','INATIVO','AFASTADO','EM_EXPERIENCIA','DESLIGADO') NOT NULL DEFAULT 'ATIVO',
    departamento_id INT NOT NULL,
    cargo_id INT NOT NULL,
    FOREIGN KEY (departamento_id) REFERENCES departamentos(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (cargo_id) REFERENCES cargos(ID) ON UPDATE CASCADE ON DELETE RESTRICT		
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    funcionario_id INT,
    username VARCHAR(250) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    funcao ENUM('ADMIN','RH','GERENTE','FUNCIONARIO') NOT NULL DEFAULT 'FUNCIONARIO',
    nome_completo VARCHAR(100),
    status ENUM('ATIVO','INATIVO') NOT NULL DEFAULT 'ATIVO',
    ultimoAcesso DATETIME,
    enderecoIp VARCHAR(45),
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE historIcoSalarios (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id INT NOT NULL,
    salario DECIMAL(10,2),
    dataInicio DATE NOT NULL,
    dataFim DATE,
    motivo VARCHAR(250),
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE registroPonto (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id INT NOT NULL,
    data_registro DATE NOT NULL,
    entrada TIME NULL,
    saida_almoco TIME NULL,
    retorno_almoco TIME NULL,
    saida TIME NULL,
    observacao TEXT,
    status ENUM('PENDENTE','COMPLETO','INCONSISTENTE','FALTA') NOT NULL DEFAULT 'PENDENTE',
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE(funcionario_id, data_registro)
);

CREATE TABLE despesas (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    funcionario_id INT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    dia DATE NOT NULL,
    descricao TEXT,
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- ======================================================
-- 3. INSERÇÃO DE DADOS
-- ======================================================

INSERT INTO departamentos (NOME, descricao) VALUES  
('Recursos Humanos', 'Gestão de pessoas e talentos'),
('Tecnologia', 'Desenvolvimento e infraestrutura'),
('Financeiro', 'Gestão de contas e tesouraria'),
('Vendas', 'Comercial e novos negócios');

INSERT INTO cargos (nome, descricao, salarioBase, nivel, ativo) VALUES  
('Analista de RH', 'Analista administrativo', 4500.00, 'Pleno', TRUE),
('Desenvolvedor Pleno', 'Dev Full Stack', 8500.00, 'Pleno', TRUE),
('Gerente de TI', 'Liderança técnica', 12000.00, 'Gerência', TRUE),
('Diretor Executivo', 'Alta diretoria', 25000.00, 'Executivo', TRUE),
('Assistente Administrativo', 'Suporte geral', 2200.00, 'Junior', TRUE);

INSERT INTO funcionarios (ID, nome, cpf, email, telefone, data_admissao, departamento_id, cargo_id) VALUES
(1, 'Alice Silva', '11122233344', 'alice@empresa.com', '11999998888', '2023-01-10', 1, 1),
(2, 'Bruno Costa', '22233344455', 'bruno@empresa.com', '11988887777', '2022-03-15', 2, 2),
(3, 'Carla Dias', '33344455566', 'carla@empresa.com', '11977776666', '2024-02-01', 3, 1),
(4, 'Daniel Oh', '44455566677', 'daniel@empresa.com', '11966665555', '2020-05-20', 2, 3),
(5, 'Eduarda Luz', '55566677788', 'eduarda@empresa.com', '11955554444', '2023-06-15', 4, 1),
(6, 'Fabio Melo', '66677788899', 'fabio@empresa.com', '11944443333', '2015-10-01', 3, 4),
(7, 'Gisele Vaz', '77788899900', 'gisele@empresa.com', '11933332222', '2023-11-20', 1, 1),
(8, 'Hugo Reis', '88899900011', 'hugo@empresa.com', '11922221111', '2021-04-12', 2, 2),
(9, 'Iara Lima', '99900011122', 'iara@empresa.com', '11911110000', '2024-01-05', 4, 1),
(10, 'Joao Neto', '00011122233', 'joao@empresa.com', '11900009999', '2022-12-01', 3, 5);

INSERT INTO historIcoSalarios (funcionario_id, salario, dataInicio, dataFim, motivo) VALUES
(1, 4500.00, '2023-01-10', NULL, 'Salário Inicial'),
(2, 8500.00, '2022-03-15', NULL, 'Contratação'),
(3, 6000.00, '2024-02-01', NULL, 'Salário Inicial'),
(4, 11000.00, '2020-05-20', '2023-05-20', 'Salário Inicial'),
(4, 12000.00, '2023-05-21', NULL, 'Promoção para Gerente'),
(5, 4000.00, '2023-06-15', NULL, 'Salário Inicial'),
(6, 25000.00, '2015-10-01', NULL, 'Contratação Diretor'),
(7, 7500.00, '2023-11-20', NULL, 'Salário Inicial'),
(8, 10500.00, '2021-04-12', NULL, 'Contratação'),
(10, 2200.00, '2022-12-01', NULL, 'Salário Inicial');

INSERT INTO registroPonto (funcionario_id, data_registro, entrada, saida_almoco, retorno_almoco, saida, status) VALUES
(1, '2024-04-15', '08:00:00', '12:00:00', '13:00:00', '17:00:00', 'COMPLETO'),
(2, '2024-04-15', '09:00:00', '13:00:00', '14:00:00', '18:00:00', 'COMPLETO'),
(3, '2024-04-15', '08:30:00', '12:00:00', '13:00:00', '17:30:00', 'COMPLETO'),
(4, '2024-04-15', '08:00:00', NULL, NULL, NULL, 'INCONSISTENTE'),
(5, '2024-04-15', '08:00:00', '12:00:00', '13:00:00', '17:00:00', 'COMPLETO'),
(6, '2024-04-15', '09:15:00', '12:30:00', '13:30:00', '18:15:00', 'COMPLETO'),
(7, '2024-04-15', '08:00:00', '12:00:00', '13:00:00', '17:00:00', 'COMPLETO'),
(8, '2024-04-15', NULL, NULL, NULL, NULL, 'FALTA'),
(9, '2024-04-15', '08:00:00', '12:00:00', '13:00:00', '17:00:00', 'COMPLETO'),
(10, '2024-04-15', '07:00:00', '11:00:00', '12:00:00', '16:00:00', 'COMPLETO');

INSERT INTO despesas (funcionario_id, valor, tipo, dia, descricao) VALUES
(2, 150.00, 'Infraestrutura', '2024-04-10', 'Compra de teclado mecânico'),
(4, 500.00, 'Viagem', '2024-04-12', 'Hospedagem em visita a cliente'),
(4, 85.50, 'Alimentação', '2024-04-12', 'Almoço com cliente'),
(1, 40.00, 'Transporte', '2024-04-14', 'Uber para evento de RH'),
(5, 200.00, 'Marketing', '2024-04-05', 'Assinatura de banco de imagens'),
(6, 1200.00, 'Viagem', '2024-04-01', 'Passagem aérea reunião diretoria'),
(8, 300.00, 'Treinamento', '2024-03-15', 'Curso online de AWS'),
(2, 50.00, 'Alimentação', '2024-04-15', 'Jantar em hora extra'),
(7, 120.00, 'Jurídico', '2024-04-16', 'Taxas de cartório'),
(10, 80.00, 'Manutenção', '2024-04-16', 'Reposição de ferramentas');

INSERT INTO usuarios (funcionario_id, username, senha, funcao, nome_completo, status, ultimoAcesso, enderecoIp) VALUES
(6, 'admin', '123', 'ADMIN', 'Fabio Melo', 'ATIVO', NOW(), '192.168.1.100'),
(1, 'alice.silva', '123', 'RH', 'Alice Silva', 'ATIVO', NOW(), '192.168.1.101'),
(7, 'gisele.vaz', '123', 'RH', 'Gisele Vaz', 'ATIVO', NOW(), '192.168.1.102'),
(4, 'daniel.oh', '123', 'GERENTE', 'Daniel Oh', 'ATIVO', NOW(), '192.168.1.103'),
(2, 'bruno.costa', '123', 'FUNCIONARIO', 'Bruno Costa', 'ATIVO', NOW(), '192.168.1.104'),
(3, 'carla.dias', '123', 'FUNCIONARIO', 'Carla Dias', 'ATIVO', NOW(), '192.168.1.105'),
(5, 'eduarda.luz', '123', 'FUNCIONARIO', 'Eduarda Luz', 'ATIVO', NOW(), '192.168.1.106'),
(8, 'hugo.reis', '123', 'FUNCIONARIO', 'Hugo Reis', 'INATIVO', NULL, NULL),
(9, 'iara.lima', '123', 'FUNCIONARIO', 'Iara Lima', 'ATIVO', NOW(), '192.168.1.107'),
(10, 'joao.neto', '123', 'FUNCIONARIO', 'Joao Neto', 'ATIVO', NOW(), '192.168.1.108');
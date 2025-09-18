SET ECHO ON

-- =====================================================
-- SCRIPT DE CRIAÇÃO DO BANCO SECAI - DASA
-- Prefixo DASA_ para evitar conflito com outros projetos
-- =====================================================

-- Drop tables se existirem (para desenvolvimento)
DROP TABLE dasa_itens_retirada CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_historico_retiradas CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_atendimentos CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_pacientes CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_insumos CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_enfermeiros CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_tecnicos CASCADE CONSTRAINTS PURGE;
DROP TABLE dasa_exames CASCADE CONSTRAINTS PURGE;

-- Drop sequences se existir
DROP SEQUENCE seq_paciente_id;
DROP SEQUENCE seq_atendimento_id;

-- =====================================================
-- CRIAÇÃO DA SEQUENCE
-- =====================================================
CREATE SEQUENCE seq_paciente_id
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE seq_atendimento_id
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- =====================================================
-- TABELAS INDEPENDENTES
-- =====================================================

-- TABELA DE EXAMES
CREATE TABLE dasa_exames (
    id          NUMBER(3) NOT NULL,
    nome        VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_exame PRIMARY KEY (id),
    CONSTRAINT uk_exame_nome UNIQUE (nome)
);

-- TABELA DE TÉCNICOS
CREATE TABLE dasa_tecnicos (
    crbm        NUMBER(5) NOT NULL,
    nome        VARCHAR2(120) NOT NULL,
    CONSTRAINT pk_tecnico PRIMARY KEY (crbm)
);

-- TABELA DE ENFERMEIROS
CREATE TABLE dasa_enfermeiros (
    coren           NUMBER(6) NOT NULL,
    nome            VARCHAR2(120) NOT NULL,
    especialidade   VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_enfermeiro PRIMARY KEY (coren),
    CONSTRAINT ck_enfermeiro_especialidade
        CHECK (especialidade IN ('Hemograma Completo', 'Exame de Urina', 'Exame de Glicemia'))
);

-- TABELA DE INSUMOS (ESTOQUE)
CREATE TABLE dasa_insumos (
    id                      NUMBER(4) NOT NULL,
    nome                    VARCHAR2(100) NOT NULL,
    codigo_barras           NUMBER(7) NOT NULL,
    quantidade_disponivel   NUMBER(4) DEFAULT 1500 NOT NULL,
    quantidade_maxima       NUMBER(4) DEFAULT 2000 NOT NULL,
    CONSTRAINT pk_insumo PRIMARY KEY (id),
    CONSTRAINT uk_insumo_codigo_barras UNIQUE (codigo_barras),
    CONSTRAINT ck_insumo_quantidade_disponivel
        CHECK (quantidade_disponivel >= 0 AND quantidade_disponivel <= 2000),
    CONSTRAINT ck_insumo_quantidade_maxima
        CHECK (quantidade_maxima = 2000)
);

-- =====================================================
-- TABELA DE PACIENTES (apenas dados básicos)
-- =====================================================
CREATE TABLE dasa_pacientes (
    id                      NUMBER DEFAULT seq_paciente_id.NEXTVAL NOT NULL,
    nome_completo           VARCHAR2(120) NOT NULL,
    cpf                     VARCHAR2(11) NOT NULL,
    data_nascimento         DATE NOT NULL,
    convenio                VARCHAR2(1) DEFAULT 'N' NOT NULL,
    preferencial            VARCHAR2(1) DEFAULT 'N' NOT NULL,
    status_paciente         VARCHAR2(10) DEFAULT 'Ativo' NOT NULL,
    CONSTRAINT pk_paciente PRIMARY KEY (id),
    CONSTRAINT uk_paciente_cpf UNIQUE (cpf),
    CONSTRAINT ck_paciente_convenio CHECK (convenio IN ('S', 'N')),
    CONSTRAINT ck_paciente_preferencial CHECK (preferencial IN ('S', 'N')),
    CONSTRAINT ck_paciente_status CHECK (status_paciente IN ('Ativo', 'Inativo'))
);


-- =====================================================
-- TABELA DE ATENDIMENTOS (múltiplos por paciente)
-- =====================================================
CREATE TABLE dasa_atendimentos (
    id                      NUMBER DEFAULT seq_atendimento_id.NEXTVAL NOT NULL,
    paciente_id             NUMBER NOT NULL,
    exame_id                NUMBER(3) NOT NULL,
    data_exame              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    jejum                   VARCHAR2(1) DEFAULT 'N' NOT NULL,
    status_atendimento      VARCHAR2(20) DEFAULT 'Em espera' NOT NULL,
    enfermeiro_coren        NUMBER(6),
    tecnico_crbm            NUMBER(5),
    CONSTRAINT pk_atendimento PRIMARY KEY (id),
    CONSTRAINT fk_atendimento_paciente FOREIGN KEY (paciente_id)
        REFERENCES dasa_pacientes(id),
    CONSTRAINT fk_atendimento_exame FOREIGN KEY (exame_id)
        REFERENCES dasa_exames(id),
    CONSTRAINT fk_atendimento_enfermeiro FOREIGN KEY (enfermeiro_coren)
        REFERENCES dasa_enfermeiros(coren),
    CONSTRAINT fk_atendimento_tecnico FOREIGN KEY (tecnico_crbm)
        REFERENCES dasa_tecnicos(crbm),
    CONSTRAINT ck_atendimento_jejum CHECK (jejum IN ('S', 'N')),
    CONSTRAINT ck_atendimento_status
        CHECK (status IN ('Em espera', 'Atendido', 'Cancelado'))
);

-- =====================================================
-- TABELA DE HISTÓRICO DE RETIRADAS
-- =====================================================
CREATE TABLE dasa_historico_retiradas (
    atendimento_id      NUMBER NOT NULL,
    data_retirada       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    tecnico_crbm        NUMBER(5) NOT NULL,
    enfermeiro_coren    NUMBER(6) NOT NULL,
    CONSTRAINT pk_historico PRIMARY KEY (atendimento_id, data_retirada),
    CONSTRAINT fk_historico_atendimento FOREIGN KEY (atendimento_id)
        REFERENCES dasa_atendimentos(id),
    CONSTRAINT fk_historico_tecnico FOREIGN KEY (tecnico_crbm)
        REFERENCES dasa_tecnicos(crbm),
    CONSTRAINT fk_historico_enfermeiro FOREIGN KEY (enfermeiro_coren)
        REFERENCES dasa_enfermeiros(coren)
);

-- =====================================================
-- TABELA DE ITENS DA RETIRADA (DETALHES)
-- =====================================================
CREATE TABLE dasa_itens_retirada (
    atendimento_id  NUMBER NOT NULL,
    data_retirada   TIMESTAMP NOT NULL,
    insumo_id       NUMBER(4) NOT NULL,
    quantidade      NUMBER(3) NOT NULL,
    CONSTRAINT pk_item_retirada PRIMARY KEY (atendimento_id, data_retirada, insumo_id),
    CONSTRAINT fk_item_historico FOREIGN KEY (atendimento_id, data_retirada)
        REFERENCES dasa_historico_retiradas(atendimento_id, data_retirada),
    CONSTRAINT fk_item_insumo FOREIGN KEY (insumo_id)
        REFERENCES dasa_insumos(id),
    CONSTRAINT ck_item_quantidade CHECK (quantidade > 0)
);

-- =====================================================
-- ÍNDICES PARA PERFORMANCE
-- =====================================================
CREATE INDEX idx_atendimento_paciente ON dasa_atendimentos(paciente_id);
CREATE INDEX idx_atendimento_status ON dasa_atendimentos(status);
CREATE INDEX idx_atendimento_data ON dasa_atendimentos(data_exame);
CREATE INDEX idx_historico_data ON dasa_historico_retiradas(data_retirada);
CREATE INDEX idx_insumo_nome ON dasa_insumos(nome);

-- =====================================================
-- COMENTÁRIOS NAS TABELAS
-- =====================================================
COMMENT ON TABLE dasa_exames IS 'Tabela de exames de um laboratório da DASA';
COMMENT ON TABLE dasa_pacientes IS 'Tabela com dados básicos de cadastro de pacientes do sistema DASA';
COMMENT ON TABLE dasa_atendimentos IS 'Tabela com os dados de atendimento aos pacientes para realização de exames';
COMMENT ON TABLE dasa_insumos IS 'Tabela de controle de estoque de insumos';
COMMENT ON TABLE dasa_historico_retiradas IS 'Tabela de Histórico de retiradas de insumos';
COMMENT ON TABLE dasa_itens_retirada IS 'Tabela de detalhes sobre a retirada de insumos';
COMMENT ON TABLE dasa_tecnicos IS 'Tabela de Técnicos de laboratório';
COMMENT ON TABLE dasa_enfermeiros IS 'Tabela de Enfermeiros por especialidade';
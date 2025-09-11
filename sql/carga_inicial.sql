    SET ECHO ON

-- =====================================================
-- SCRIPT DE CARGA INICIAL - SISTEMA DASA
-- =====================================================

-- Limpar dados existentes
DELETE FROM dasa_itens_retirada;
DELETE FROM dasa_historico_retiradas;
DELETE FROM dasa_atendimentos;
DELETE FROM dasa_pacientes;
DELETE FROM dasa_insumos;
DELETE FROM dasa_enfermeiros;
DELETE FROM dasa_tecnicos;
DELETE FROM dasa_exames;

-- =====================================================
-- INSERIR EXAMES
-- =====================================================
INSERT INTO dasa_exames (id, nome) VALUES (123, 'Hemograma Completo');
INSERT INTO dasa_exames (id, nome) VALUES (456, 'Exame de Urina');
INSERT INTO dasa_exames (id, nome) VALUES (789, 'Exame de Glicemia');

-- =====================================================
-- INSERIR TÉCNICOS
-- =====================================================
INSERT INTO dasa_tecnicos (crbm, nome) VALUES (12345, 'João Silva');
INSERT INTO dasa_tecnicos (crbm, nome) VALUES (67890, 'Maria Santos');
INSERT INTO dasa_tecnicos (crbm, nome) VALUES (11223, 'Pedro Oliveira');

-- =====================================================
-- INSERIR ENFERMEIROS
-- =====================================================
-- Hemograma Completo
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (741321, 'Ana Carolina Silva', 'Hemograma Completo');
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (741322, 'Roberto Fernandes', 'Hemograma Completo');

-- Exame de Urina
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (852431, 'Mariana Costa', 'Exame de Urina');
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (852432, 'Carlos Eduardo', 'Exame de Urina');

-- Exame de Glicemia
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (963541, 'Juliana Santos', 'Exame de Glicemia');
INSERT INTO dasa_enfermeiros (coren, nome, especialidade)
VALUES (963542, 'Fernando Lima', 'Exame de Glicemia');

-- =====================================================
-- INSERIR INSUMOS COM ESTOQUE INICIAL DE 1500
-- =====================================================
-- Tubos de coleta (Hemograma Completo)
INSERT INTO dasa_insumos VALUES (1051, 'Tubo de Coleta Pequeno', 1000051, 1500, 2000);
INSERT INTO dasa_insumos VALUES (1052, 'Tubo de Coleta Médio', 1000052, 1500, 2000);
INSERT INTO dasa_insumos VALUES (1053, 'Tubo de Coleta Grande', 1000053, 1500, 2000);

-- Agulhas (Hemograma Completo e Glicemia)
INSERT INTO dasa_insumos VALUES (2071, 'Agulha 2mm', 2000071, 1500, 2000);
INSERT INTO dasa_insumos VALUES (2072, 'Agulha 3mm', 2000072, 1500, 2000);
INSERT INTO dasa_insumos VALUES (2073, 'Agulha 5mm', 2000073, 1500, 2000);

-- Seringas (Hemograma Completo e Glicemia)
INSERT INTO dasa_insumos VALUES (3081, 'Seringa 5ml', 3000081, 1500, 2000);
INSERT INTO dasa_insumos VALUES (3082, 'Seringa 10ml', 3000082, 1500, 2000);
INSERT INTO dasa_insumos VALUES (3083, 'Seringa 20ml', 3000083, 1500, 2000);

-- Recipiente estéril (Exame de Urina)
INSERT INTO dasa_insumos VALUES (4091, 'Recipiente Estéril Pequeno', 4000091, 1500, 2000);
INSERT INTO dasa_insumos VALUES (4092, 'Recipiente Estéril Médio', 4000092, 1500, 2000);
INSERT INTO dasa_insumos VALUES (4093, 'Recipiente Estéril Grande', 4000093, 1500, 2000);

-- Tiras reagentes (Exame de Urina e Glicemia)
INSERT INTO dasa_insumos VALUES (5001, 'Tira Reagente Tipo A', 5000001, 1500, 2000);
INSERT INTO dasa_insumos VALUES (5002, 'Tira Reagente Tipo B', 5000002, 1500, 2000);
INSERT INTO dasa_insumos VALUES (5003, 'Tira Reagente Tipo C', 5000003, 1500, 2000);

-- Lâmina para análise (Exame de Urina)
INSERT INTO dasa_insumos VALUES (6011, 'Lâmina Análise Simples', 6000011, 1500, 2000);
INSERT INTO dasa_insumos VALUES (6012, 'Lâmina Análise Dupla', 6000012, 1500, 2000);
INSERT INTO dasa_insumos VALUES (6013, 'Lâmina Análise Tripla', 6000013, 1500, 2000);

-- Tubo sem anticoagulante (Exame de Glicemia)
INSERT INTO dasa_insumos VALUES (7021, 'Tubo sem Anticoagulante Pequeno', 7000021, 1500, 2000);
INSERT INTO dasa_insumos VALUES (7022, 'Tubo sem Anticoagulante Médio', 7000022, 1500, 2000);
INSERT INTO dasa_insumos VALUES (7023, 'Tubo sem Anticoagulante Grande', 7000023, 1500, 2000);

COMMIT;
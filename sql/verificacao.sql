SET ECHO ON

-- =====================================================
-- SCRIPT DE VERIFICAÇÃO - SISTEMA DASA
-- =====================================================

-- Verificar sequences
SELECT sequence_name, last_number FROM user_sequences
WHERE sequence_name = 'SEQ_PACIENTE_ID';

SELECT sequence_name, last_number FROM user_sequences
WHERE sequence_name = 'SEQ_ATENDIMENTO_ID';

-- Contar registros
SELECT 'Exames' AS tabela, COUNT(*) AS total FROM dasa_exames
UNION ALL
SELECT 'Técnicos', COUNT(*) FROM dasa_tecnicos
UNION ALL
SELECT 'Enfermeiros', COUNT(*) FROM dasa_enfermeiros
UNION ALL
SELECT 'Insumos', COUNT(*) FROM dasa_insumos
UNION ALL
SELECT 'Pacientes', COUNT(*) FROM dasa_pacientes;

-- Verificar estoque
SELECT nome, quantidade_disponivel, quantidade_maxima
FROM dasa_insumos
ORDER BY id;

-- Verificar enfermeiros por especialidade
SELECT especialidade, COUNT(*) as total
FROM dasa_enfermeiros
GROUP BY especialidade;

ALTER SESSION SET NLS_DATE_FORMAT = 'DD/MM/YYYY';

SELECT * FROM dasa_enfermeiros;
SELECT * FROM dasa_exames;
SELECT * FROM dasa_historico_retiradas;
SELECT * FROM dasa_insumos;
SELECT * FROM dasa_itens_retirada;
SELECT * FROM dasa_pacientes;
SELECT * FROM dasa_atendimentos;
SELECT * FROM dasa_tecnicos;


-- =====================================================
-- QUERY FORMATADA PARA VISUALIZAR MELHOR OS DADOS
-- =====================================================

-- Query para visualizar pacientes:
SELECT
    id,
    nome_completo,
    cpf as cpf_formatado,
    TO_CHAR(data_nascimento, 'DD/MM/YYYY') as data_nasc,
    convenio,
    preferencial,
    status_paciente
FROM dasa_pacientes
ORDER BY id;

-- Query para visualizar atendimentos:
SELECT
    a.id,
    p.nome_completo,
    p.cpf,
    e.nome as exame,
    TO_CHAR(a.data_exame, 'DD/MM/YYYY HH24:MI') as data_exame,
    a.jejum,
    a.status_atendimento,
    NVL(TO_CHAR(a.enfermeiro_coren), 'Em espera') as enfermeiro,
    NVL(TO_CHAR(a.tecnico_crbm), 'Em espera') as tecnico
FROM dasa_atendimentos a
JOIN dasa_pacientes p ON a.paciente_id = p.id
JOIN dasa_exames e ON a.exame_id = e.id
ORDER BY a.id;
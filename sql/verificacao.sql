-- =====================================================
-- SCRIPT DE VERIFICAÇÃO - SISTEMA DASA
-- =====================================================

-- Verificar sequences
SELECT sequence_name, last_number FROM user_sequences;

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
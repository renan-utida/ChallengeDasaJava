package com.dasa.controller.dao.jdbc;

import com.dasa.model.funcionarios.Enfermeiro;
import com.dasa.model.funcionarios.TecnicoLaboratorio;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - JdbcFuncionarioDao com Mock")
public class JdbcFuncionarioDaoTest {

    private JdbcFuncionarioDao dao;

    @BeforeEach
    public void setUp() {
        dao = new JdbcFuncionarioDao();
    }

    @Test
    @DisplayName("Deve validar SQL busca técnico por CRBM")
    public void testValidarSqlBuscarTecnicoPorCrbm() {
        String sql = "SELECT * FROM dasa_tecnicos WHERE crbm = ?";

        assertTrue(sql.contains("dasa_tecnicos"));
        assertTrue(sql.contains("WHERE crbm = ?"));
    }

    @Test
    @DisplayName("Deve validar SQL listar todos técnicos")
    public void testValidarSqlListarTodosTecnicos() {
        String sql = "SELECT * FROM dasa_tecnicos ORDER BY nome";

        assertTrue(sql.contains("dasa_tecnicos"));
        assertTrue(sql.contains("ORDER BY nome"));
    }

    @Test
    @DisplayName("Deve validar SQL busca enfermeiro por COREN")
    public void testValidarSqlBuscarEnfermeiroPorCoren() {
        String sql = "SELECT * FROM dasa_enfermeiros WHERE coren = ?";

        assertTrue(sql.contains("dasa_enfermeiros"));
        assertTrue(sql.contains("WHERE coren = ?"));
    }

    @Test
    @DisplayName("Deve validar SQL listar enfermeiros por especialidade")
    public void testValidarSqlListarPorEspecialidade() {
        String sql = "SELECT * FROM dasa_enfermeiros WHERE especialidade = ? ORDER BY nome";

        assertTrue(sql.contains("WHERE especialidade = ?"));
        assertTrue(sql.contains("ORDER BY nome"));
    }

    @Test
    @DisplayName("Deve criar objeto TecnicoLaboratorio")
    public void testCriacaoTecnico() {
        TecnicoLaboratorio tecnico = new TecnicoLaboratorio("João Silva", 12345);

        assertEquals("João Silva", tecnico.getNome());
        assertEquals(12345, tecnico.getCrbm());
        assertEquals(12345, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve criar objeto Enfermeiro")
    public void testCriacaoEnfermeiro() {
        Enfermeiro enfermeiro = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");

        assertEquals("Ana Silva", enfermeiro.getNome());
        assertEquals(741321, enfermeiro.getCoren());
        assertEquals("Hemograma Completo", enfermeiro.getEspecialidade());
        assertEquals(741321, enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve validar sincronização registro-CRBM")
    public void testSincronizacaoRegistroCrbm() {
        TecnicoLaboratorio tecnico = new TecnicoLaboratorio();
        tecnico.setCrbm(99999);

        assertEquals(99999, tecnico.getCrbm());
        assertEquals(99999, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve validar sincronização registro-COREN")
    public void testSincronizacaoRegistroCoren() {
        Enfermeiro enfermeiro = new Enfermeiro();
        enfermeiro.setCoren(88888);

        assertEquals(88888, enfermeiro.getCoren());
        assertEquals(88888, enfermeiro.getRegistro());
    }
}
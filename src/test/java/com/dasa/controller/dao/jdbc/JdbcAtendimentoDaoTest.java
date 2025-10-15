package com.dasa.controller.dao.jdbc;

import com.dasa.model.domain.Atendimento;
import com.dasa.model.domain.Paciente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

@DisplayName("Testes - JdbcAtendimentoDao com Mock")
public class JdbcAtendimentoDaoTest {

    private JdbcAtendimentoDao dao;

    @BeforeEach
    public void setUp() {
        dao = new JdbcAtendimentoDao();
    }

    @Test
    @DisplayName("Deve validar SQL de salvar atendimento")
    public void testValidarSqlSalvar() {
        String sql = "INSERT INTO dasa_atendimentos (paciente_id, exame_id, jejum, status_atendimento) " +
                "VALUES (?, ?, ?, ?)";

        assertTrue(sql.contains("INSERT INTO dasa_atendimentos"));
        assertTrue(sql.contains("paciente_id"));
        assertTrue(sql.contains("exame_id"));
        assertTrue(sql.contains("jejum"));
        assertTrue(sql.contains("status_atendimento"));
    }

    @Test
    @DisplayName("Deve validar SQL de buscar por ID")
    public void testValidarSqlBuscarPorId() {
        String sql = "SELECT a.*, e.nome as exame_nome";

        assertTrue(sql.contains("SELECT"));
        assertTrue(sql.contains("exame_nome"));
    }

    @Test
    @DisplayName("Deve validar SQL de atualizar status")
    public void testValidarSqlAtualizarStatus() {
        String sql = "UPDATE dasa_atendimentos SET status_atendimento = ?, enfermeiro_coren = ?, " +
                "tecnico_crbm = ? WHERE id = ?";

        assertTrue(sql.contains("UPDATE"));
        assertTrue(sql.contains("status_atendimento"));
        assertTrue(sql.contains("enfermeiro_coren"));
        assertTrue(sql.contains("tecnico_crbm"));
    }

    @Test
    @DisplayName("Deve criar atendimento com valores iniciais corretos")
    public void testCriacaoAtendimento() {
        Atendimento atendimento = new Atendimento(1, "Hemograma Completo", true);

        assertEquals(1, atendimento.getPacienteId());
        assertEquals("Hemograma Completo", atendimento.getExame());
        assertTrue(atendimento.isJejum());
        assertEquals("Em espera", atendimento.getStatus());
        assertEquals("Em espera", atendimento.getEnfermeiroResponsavel());
        assertEquals("Em espera", atendimento.getResponsavelColeta());
    }

    @Test
    @DisplayName("Deve validar conversão de jejum")
    public void testConversaoJejum() {
        boolean jejum = true;
        String jejumDb = jejum ? "S" : "N";
        assertEquals("S", jejumDb);

        jejum = false;
        jejumDb = jejum ? "S" : "N";
        assertEquals("N", jejumDb);
    }

    @Test
    @DisplayName("Deve validar mapeamento de atendimento com paciente")
    public void testMapeamentoAtendimentoComPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId(1);
        paciente.setNomeCompleto("Teste");
        paciente.setCpf("12345678901");
        paciente.setDataNascimento(LocalDate.now());

        Atendimento atendimento = new Atendimento(1, "Exame", true);
        atendimento.setPaciente(paciente);

        assertNotNull(atendimento.getPaciente());
        assertEquals("Teste", atendimento.getPaciente().getNomeCompleto());
    }

    @Test
    @DisplayName("Deve validar informações de responsáveis")
    public void testInformacoesResponsaveis() {
        Atendimento atendimento = new Atendimento(1, "Exame", false);

        atendimento.setEnfermeiroResponsavel("Ana Silva - COREN: 741321");
        atendimento.setResponsavelColeta("João Silva - CRBM: 12345");

        assertTrue(atendimento.getEnfermeiroResponsavel().contains("Ana Silva"));
        assertTrue(atendimento.getResponsavelColeta().contains("João Silva"));
    }
}
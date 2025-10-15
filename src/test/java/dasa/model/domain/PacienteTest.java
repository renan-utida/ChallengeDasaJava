package dasa.model.domain;

import com.dasa.model.domain.Paciente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DisplayName("Testes - classe Paciente")
public class PacienteTest {

    private Paciente paciente;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    public void setUp() {
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setNomeCompleto("João Silva");
        paciente.setCpf("12345678901");
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente.setConvenio(true);
        paciente.setPreferencial(false);
    }

    @Test
    @DisplayName("Deve criar paciente com construtor completo")
    public void testConstrutorCompleto() {
        Paciente p = new Paciente("Maria Santos", "98765432100", "15/05/1985", true, true);

        assertEquals("Maria Santos", p.getNomeCompleto());
        assertEquals("98765432100", p.getCpf());
        assertEquals(LocalDate.of(1985, 5, 15), p.getDataNascimento());
        assertTrue(p.isConvenio());
        assertTrue(p.isPreferencial());
        assertEquals("Ativo", p.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve formatar CPF corretamente")
    public void testGetCpfFormatado() {
        assertEquals("123.456.789-01", paciente.getCpfFormatado());
    }

    @Test
    @DisplayName("Deve retornar CPF sem formatação quando inválido")
    public void testGetCpfFormatadoInvalido() {
        paciente.setCpf("123");
        assertEquals("123", paciente.getCpfFormatado());

        paciente.setCpf(null);
        assertNull(paciente.getCpfFormatado());
    }

    @Test
    @DisplayName("Deve formatar data de nascimento")
    public void testGetDataNascimentoFormatada() {
        assertEquals("01/01/1990", paciente.getDataNascimentoFormatada());
    }

    @Test
    @DisplayName("Deve ter status Ativo por padrão")
    public void testStatusPadraoAtivo() {
        Paciente novoPaciente = new Paciente();
        assertEquals("Ativo", novoPaciente.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve alterar status do paciente")
    public void testAlterarStatus() {
        paciente.setStatusPaciente("Inativo");
        assertEquals("Inativo", paciente.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve alternar convênio")
    public void testAlternarConvenio() {
        assertTrue(paciente.isConvenio());
        paciente.setConvenio(false);
        assertFalse(paciente.isConvenio());
    }
}
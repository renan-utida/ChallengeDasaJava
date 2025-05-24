package dasa.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PacienteTest {
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente("João Silva", 12345678901L, "15/03/1990",
                true, false, true, "Hemograma Completo");
    }

    @Test
    void testConstrutorComParametros() {
        assertEquals("João Silva", paciente.getNomeCompleto());
        assertEquals(12345678901L, paciente.getCpf());
        assertEquals("15/03/1990", paciente.getDataNascimento());
        assertTrue(paciente.isConvenio());
        assertFalse(paciente.isPreferencial());
        assertTrue(paciente.isJejum());
        assertEquals("Hemograma Completo", paciente.getExame());
        assertEquals("Em espera", paciente.getStatus());
        assertEquals("Em espera", paciente.getEnfermeiroResponsavel());
        assertEquals("Em espera", paciente.getResponsavelColeta());
        assertNotNull(paciente.getDataExame());
        assertTrue(paciente.getId() > 0);
    }

    @Test
    void testValidarCPFValido() {
        assertTrue(Paciente.validarCPF(12345678901L));
        assertTrue(Paciente.validarCPF(98765432100L));
    }

    @Test
    void testValidarCPFInvalido() {
        assertFalse(Paciente.validarCPF(123456789L)); // 9 dígitos
        assertFalse(Paciente.validarCPF(123456789012L)); // 12 dígitos
        assertFalse(Paciente.validarCPF(0L)); // 0
    }

    @Test
    void testGetCpfFormatado() {
        String cpfFormatado = paciente.getCpfFormatado();
        assertEquals("123.456.789-01", cpfFormatado);

        // Teste com CPF menor (com zeros à esquerda)
        Paciente pacienteCpfPequeno = new Paciente("Maria", 1234567890L, "01/01/1980",
                false, false, false, "Exame de Urina");
        assertEquals("012.345.678-90", pacienteCpfPequeno.getCpfFormatado());
    }

    @Test
    void testSettersGetters() {
        paciente.setNomeCompleto("Maria Santos");
        paciente.setCpf(98765432100L);
        paciente.setDataNascimento("20/05/1985");
        paciente.setConvenio(false);
        paciente.setPreferencial(true);
        paciente.setJejum(false);
        paciente.setExame("Exame de Urina");
        paciente.setStatus("Atendido");
        paciente.setEnfermeiroResponsavel("Ana Silva - 741321");
        paciente.setResponsavelColeta("João Técnico - 12345");

        assertEquals("Maria Santos", paciente.getNomeCompleto());
        assertEquals(98765432100L, paciente.getCpf());
        assertEquals("20/05/1985", paciente.getDataNascimento());
        assertFalse(paciente.isConvenio());
        assertTrue(paciente.isPreferencial());
        assertFalse(paciente.isJejum());
        assertEquals("Exame de Urina", paciente.getExame());
        assertEquals("Atendido", paciente.getStatus());
        assertEquals("Ana Silva - 741321", paciente.getEnfermeiroResponsavel());
        assertEquals("João Técnico - 12345", paciente.getResponsavelColeta());
    }

    @Test
    void testParaStringArquivo() {
        paciente.setStatus("Atendido");
        String resultado = paciente.paraStringArquivo();

        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("12345678901"));
        assertTrue(resultado.contains("15/03/1990"));
        assertTrue(resultado.contains("true")); // convenio
        assertTrue(resultado.contains("false")); // preferencial
        assertTrue(resultado.contains("Hemograma Completo"));
        assertTrue(resultado.contains("Atendido"));
    }

    @Test
    void testConstrutorCarregamento() {
        Paciente pacienteCarregado = new Paciente(
                1, "Maria Silva", 98765432100L, "10/10/1975",
                "23/05/2025 14:30", false, true, false,
                "Exame de Glicemia", "Em espera", "Em espera", "Em espera"
        );

        assertEquals(1, pacienteCarregado.getId());
        assertEquals("Maria Silva", pacienteCarregado.getNomeCompleto());
        assertEquals(98765432100L, pacienteCarregado.getCpf());
        assertEquals("10/10/1975", pacienteCarregado.getDataNascimento());
        assertEquals("23/05/2025 14:30", pacienteCarregado.getDataExame());
        assertFalse(pacienteCarregado.isConvenio());
        assertTrue(pacienteCarregado.isPreferencial());
        assertFalse(pacienteCarregado.isJejum());
        assertEquals("Exame de Glicemia", pacienteCarregado.getExame());
        assertEquals("Em espera", pacienteCarregado.getStatus());
    }

    @Test
    void testSobrecargaExibirDados() {
        // Teste que os métodos sobrecarregados não causam exceções
        assertDoesNotThrow(() -> {
            paciente.exibirDados(); // Método original
            paciente.exibirDados(true); // Sobrecarga com boolean
            paciente.exibirDados(false); // Sobrecarga com boolean
            paciente.exibirDados("basico"); // Sobrecarga com String
            paciente.exibirDados("medico"); // Sobrecarga com String
            paciente.exibirDados("administrativo"); // Sobrecarga com String
            paciente.exibirDados("categoria_inexistente"); // Fallback
        });
    }
}
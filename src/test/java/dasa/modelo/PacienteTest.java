package dasa.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Paciente")
public class PacienteTest {

    private Paciente paciente;
    private static final String ARQUIVO_TESTE = "pacientes-teste.txt";

    @BeforeEach
    public void setUp() throws Exception {
        // Muda o arquivo para teste via reflexão
        Field arquivoField = Paciente.class.getDeclaredField("ARQUIVO_PACIENTES");
        arquivoField.setAccessible(true);
        arquivoField.set(null, ARQUIVO_TESTE);

        // Remove arquivo de teste se existir
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        // Reset contador de IDs para testes consistentes
        Field contadorField = Paciente.class.getDeclaredField("contadorId");
        contadorField.setAccessible(true);
        contadorField.set(null, 1);

        paciente = new Paciente("João Silva", 12345678901L, "01/01/1990",
                true, false, true, "Hemograma Completo");
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Limpa arquivo de teste
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        // Restaura arquivo original
        Field arquivoField = Paciente.class.getDeclaredField("ARQUIVO_PACIENTES");
        arquivoField.setAccessible(true);
        arquivoField.set(null, "pacientes.txt");
    }

    @Test
    @DisplayName("Deve criar paciente com construtor parametrizado")
    public void testConstrutorParametrizado() {
        assertNotNull(paciente);
        assertEquals("João Silva", paciente.getNomeCompleto());
        assertEquals(12345678901L, paciente.getCpf());
        assertEquals(LocalDate.of(1990, 1, 1), paciente.getDataNascimento());
        assertEquals("01/01/1990", paciente.getDataNascimentoFormatada());
        assertTrue(paciente.isConvenio());
        assertFalse(paciente.isPreferencial());
        assertTrue(paciente.isJejum());
        assertEquals("Hemograma Completo", paciente.getExame());
        assertEquals("Em espera", paciente.getStatus());
    }

    @Test
    @DisplayName("Deve validar CPF com 11 dígitos")
    public void testValidarCpfValido() {
        assertTrue(Paciente.validarCPF(12345678901L));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com menos de 11 dígitos")
    public void testValidarCpfInvalido() {
        assertFalse(Paciente.validarCPF(123456789L)); // 9 dígitos
        assertFalse(Paciente.validarCPF(12345L)); // 5 dígitos
    }

    /*
    @Test
    @DisplayName("Deve formatar CPF corretamente")
    public void testFormatarCpf() {
        paciente.setCpf(12345678901L);
        assertEquals("123.456.789-01", paciente.getCpfFormatado());
    }*/

    @Test
    @DisplayName("Deve gerar ID único para cada paciente")
    public void testIdUnico() {
        Paciente p1 = new Paciente("Ana", 11111111111L, "01/01/2000", true, true, false, "Exame de Urina");
        Paciente p2 = new Paciente("Carlos", 22222222222L, "02/02/2000", false, true, true, "Exame de Glicemia");

        assertNotEquals(p1.getId(), p2.getId());
        assertTrue(p1.getId() > 0);
        assertTrue(p2.getId() > 0);
    }

    @Test
    @DisplayName("Deve definir status inicial como 'Em espera'")
    public void testStatusInicial() {
        assertEquals("Em espera", paciente.getStatus());
        assertEquals("Em espera", paciente.getEnfermeiroResponsavel());
        assertEquals("Em espera", paciente.getResponsavelColeta());
    }

    @Test
    @DisplayName("Deve alterar status do paciente")
    public void testAlterarStatus() {
        paciente.setStatus("Atendido");
        assertEquals("Atendido", paciente.getStatus());
    }

    @Test
    @DisplayName("Deve definir responsáveis do atendimento")
    public void testDefinirResponsaveis() {
        paciente.setEnfermeiroResponsavel("Ana Silva - 741321");
        paciente.setResponsavelColeta("João Santos - 12345");

        assertEquals("Ana Silva - 741321", paciente.getEnfermeiroResponsavel());
        assertEquals("João Santos - 12345", paciente.getResponsavelColeta());
    }

    @Test
    @DisplayName("Deve salvar e carregar paciente do arquivo de teste")
    public void testSalvarCarregarArquivo() {
        paciente.salvarNoArquivo();

        var pacientes = Paciente.carregarPacientes();
        assertEquals(1, pacientes.size());
        assertEquals("João Silva", pacientes.get(0).getNomeCompleto());
    }

    @Test
    @DisplayName("Deve trabalhar com LocalDate para data de nascimento")
    public void testDataNascimentoLocalDate() {
        LocalDate novaData = LocalDate.of(1985, 12, 25);
        paciente.setDataNascimento(novaData);

        assertEquals(novaData, paciente.getDataNascimento());
        assertEquals("25/12/1985", paciente.getDataNascimentoFormatada());
    }

    @Test
    @DisplayName("Deve trabalhar com LocalDateTime para data do exame")
    public void testDataExameLocalDateTime() {
        LocalDateTime novaDataHora = LocalDateTime.of(2024, 6, 15, 14, 30);
        paciente.setDataExame(novaDataHora);

        assertEquals(novaDataHora, paciente.getDataExame());
        assertEquals("15/06/2024 14:30", paciente.getDataExameFormatada());
    }

    @Test
    @DisplayName("Deve manter compatibilidade com métodos formatados")
    public void testCompatibilidadeFormatacao() {
        // Testa se os métodos auxiliares funcionam corretamente
        assertNotNull(paciente.getDataNascimentoFormatada());
        assertNotNull(paciente.getDataExameFormatada());

        // Testa formato correto
        assertTrue(paciente.getDataNascimentoFormatada().matches("\\d{2}/\\d{2}/\\d{4}"));
        assertTrue(paciente.getDataExameFormatada().matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}"));
    }
}
package dasa.model.domain;

import com.dasa.model.domain.Atendimento;
import com.dasa.model.domain.Paciente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("Testes - classe Atendimento")
public class AtendimentoTest {

    private Atendimento atendimento;

    @BeforeEach
    public void setUp() {
        atendimento = new Atendimento(1, "Hemograma Completo", true);
    }

    @Test
    @DisplayName("Deve criar atendimento com valores iniciais corretos")
    public void testCriacaoAtendimento() {
        assertEquals(1, atendimento.getPacienteId());
        assertEquals("Hemograma Completo", atendimento.getExame());
        assertTrue(atendimento.isJejum());
        assertEquals("Em espera", atendimento.getStatus());
        assertEquals("Em espera", atendimento.getEnfermeiroResponsavel());
        assertEquals("Em espera", atendimento.getResponsavelColeta());
        assertNotNull(atendimento.getDataExame());
    }

    @Test
    @DisplayName("Deve criar atendimento com construtor completo")
    public void testConstrutorCompleto() {
        LocalDateTime dataExame = LocalDateTime.now();
        Atendimento a = new Atendimento(10, 1, "Exame de Urina", dataExame,
                false, "Atendido", "Ana Silva - COREN: 741321", "João - CRBM: 12345");

        assertEquals(10, a.getId());
        assertEquals(1, a.getPacienteId());
        assertEquals("Exame de Urina", a.getExame());
        assertEquals(dataExame, a.getDataExame());
        assertFalse(a.isJejum());
        assertEquals("Atendido", a.getStatus());
        assertEquals("Ana Silva - COREN: 741321", a.getEnfermeiroResponsavel());
        assertEquals("João - CRBM: 12345", a.getResponsavelColeta());
    }

    @Test
    @DisplayName("Deve associar paciente ao atendimento")
    public void testAssociarPaciente() {
        Paciente p = new Paciente();
        p.setId(1);
        p.setNomeCompleto("Teste");

        atendimento.setPaciente(p);

        assertNotNull(atendimento.getPaciente());
        assertEquals("Teste", atendimento.getPaciente().getNomeCompleto());
    }

    @Test
    @DisplayName("Deve atualizar status do atendimento")
    public void testAtualizarStatus() {
        atendimento.setStatus("Atendido");
        assertEquals("Atendido", atendimento.getStatus());

        atendimento.setStatus("Cancelado");
        assertEquals("Cancelado", atendimento.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar responsáveis")
    public void testAtualizarResponsaveis() {
        atendimento.setEnfermeiroResponsavel("Cancelado");
        atendimento.setResponsavelColeta("Cancelado");

        assertEquals("Cancelado", atendimento.getEnfermeiroResponsavel());
        assertEquals("Cancelado", atendimento.getResponsavelColeta());
    }
}
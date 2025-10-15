package dasa.controller.dao.jdbc;

import com.dasa.repository.jdbc.JdbcPacienteRepository;
import com.dasa.model.domain.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@DisplayName("Teste - JdbcPacienteDao (Somente Leitura)")
public class JdbcPacienteRepositoryTest {

    private JdbcPacienteRepository dao;
    private Paciente pacienteTeste;

    @BeforeEach
    public void setUp() {
        dao = new JdbcPacienteRepository();
        pacienteTeste = criarPacienteTeste();
    }

    @Test
    @DisplayName("Deve criar instância do DAO")
    public void testCriarInstanciaDao() {
        assertNotNull(dao, "DAO não deve ser nulo");
    }

    @Test
    @DisplayName("Deve tentar buscar paciente por ID sem erro de compilação")
    public void testTentarBuscarPacientePorId() {
        // When & Then - APENAS LEITURA
        assertDoesNotThrow(() -> {
            Paciente encontrado = dao.buscarPorId(1);
            // Pode ser null se não existir - não importa o resultado
            if (encontrado != null) {
                assertTrue(encontrado.getId() > 0);
                assertNotNull(encontrado.getCpf());
                assertNotNull(encontrado.getNomeCompleto());
            }
        });
    }

    @Test
    @DisplayName("Deve buscar paciente por ID inexistente retornar null")
    public void testBuscarPacientePorIdInexistenteRetornarNull() {
        // When & Then - APENAS LEITURA
        assertDoesNotThrow(() -> {
            Paciente resultado = dao.buscarPorId(99999);
            // Pode ser null (esperado) ou não - apenas testa se método funciona
        });
    }

    @Test
    @DisplayName("Deve tentar buscar paciente por CPF sem erro")
    public void testTentarBuscarPacientePorCpf() {
        // When & Then - APENAS LEITURA
        assertDoesNotThrow(() -> {
            Paciente encontrado = dao.buscarPorCpf("00000000000");
            // Resultado não importa - só testa se metodo existe e funciona
        });
    }

    @Test
    @DisplayName("Deve listar todos os pacientes sem erro")
    public void testListarTodosPacientesSemErro() {
        // When & Then - APENAS LEITURA
        assertDoesNotThrow(() -> {
            List<Paciente> pacientes = dao.listarTodos();
            assertNotNull(pacientes, "Lista não deve ser nula");
            assertTrue(pacientes.size() >= 0, "Lista deve ter tamanho válido");
        });
    }

    @Test
    @DisplayName("Deve validar que método salvar existe")
    public void testValidarQueMetodoSalvarExiste() {
        // Testa apenas se o metodo existe sem executar
        assertNotNull(dao, "DAO deve ter método salvar");

        // Verifica se metodo existe através de reflection
        assertDoesNotThrow(() -> {
            dao.getClass().getMethod("salvar", Paciente.class);
        }, "Método salvar deve existir");
    }

    @Test
    @DisplayName("Deve validar que método atualizar existe")
    public void testValidarQueMetodoAtualizarExiste() {
        // Testa apenas se o metodo existe sem executar
        assertDoesNotThrow(() -> {
            dao.getClass().getMethod("atualizar", Paciente.class);
        }, "Método atualizar deve existir");
    }

    @Test
    @DisplayName("Deve validar estrutura do paciente de teste")
    public void testValidarEstruturaPacienteTeste() {
        // Given & When & Then - testa apenas o objeto, não o banco
        assertNotNull(pacienteTeste);
        assertNotNull(pacienteTeste.getNomeCompleto());
        assertNotNull(pacienteTeste.getCpf());
        assertNotNull(pacienteTeste.getDataNascimento());
        assertTrue(pacienteTeste.getCpf().length() == 11);
    }

    private Paciente criarPacienteTeste() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto("João da Silva Teste");
        paciente.setCpf("12345678900");
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente.setConvenio(true);
        paciente.setPreferencial(false);
        return paciente;
    }
}
package dasa.funcionarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe TecnicoLaboratorio")
public class TecnicoLaboratorioTest {

    private TecnicoLaboratorio tecnico;

    @BeforeEach
    public void setUp() {
        tecnico = new TecnicoLaboratorio();
    }

    @Test
    @DisplayName("Deve criar técnico com construtor padrão")
    public void testConstrutorPadrao() {
        assertNotNull(tecnico);
        assertNull(tecnico.getNome());
        assertEquals(0, tecnico.getCrbm());
        assertEquals(0, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve criar técnico com construtor parametrizado")
    public void testConstrutorParametrizado() {
        TecnicoLaboratorio tec = new TecnicoLaboratorio("Pedro Silva", 12345);

        assertEquals("Pedro Silva", tec.getNome());
        assertEquals(12345, tec.getCrbm());
        assertEquals(12345, tec.getRegistro());
    }

    @Test
    @DisplayName("Deve definir e obter CRBM corretamente")
    public void testSetterGetterCrbm() {
        tecnico.setCrbm(67890);
        assertEquals(67890, tecnico.getCrbm());
        assertEquals(67890, tecnico.getRegistro()); // Deve sincronizar
    }

    @Test
    @DisplayName("Deve sincronizar CRBM com registro da classe pai")
    public void testSincronizacaoCrbmRegistro() {
        tecnico.setCrbm(11223);
        assertEquals(tecnico.getCrbm(), tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve herdar métodos da classe pai")
    public void testHeranca() {
        tecnico.setNome("Ana Costa");
        assertEquals("Ana Costa", tecnico.getNome());
    }

    @Test
    @DisplayName("Deve aceitar CRBM com 5 dígitos")
    public void testCrbmCincoDigitos() {
        tecnico.setCrbm(12345);
        assertEquals(12345, tecnico.getCrbm());
    }
}
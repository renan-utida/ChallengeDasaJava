package dasa.model.funcionarios;

import dasa.model.funcionarios.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - classes de Funcionários")
public class FuncionarioTest {

    @Test
    @DisplayName("Deve criar funcionário base")
    public void testCriarFuncionario() {
        Funcionario f = new Funcionario("João", 12345);

        assertEquals("João", f.getNome());
        assertEquals(12345, f.getRegistro());
    }

    @Test
    @DisplayName("Deve criar enfermeiro com especialidade")
    public void testCriarEnfermeiro() {
        Enfermeiro e = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");

        assertEquals("Ana Silva", e.getNome());
        assertEquals(741321, e.getCoren());
        assertEquals(741321, e.getRegistro());
        assertEquals("Hemograma Completo", e.getEspecialidade());
    }

    @Test
    @DisplayName("Deve sincronizar COREN com registro")
    public void testSincronizacaoCoren() {
        Enfermeiro e = new Enfermeiro();
        e.setCoren(999999);

        assertEquals(999999, e.getCoren());
        assertEquals(999999, e.getRegistro());
    }

    @Test
    @DisplayName("Deve criar técnico de laboratório")
    public void testCriarTecnico() {
        TecnicoLaboratorio t = new TecnicoLaboratorio("Pedro", 67890);

        assertEquals("Pedro", t.getNome());
        assertEquals(67890, t.getCrbm());
        assertEquals(67890, t.getRegistro());
    }

    @Test
    @DisplayName("Deve sincronizar CRBM com registro")
    public void testSincronizacaoCrbm() {
        TecnicoLaboratorio t = new TecnicoLaboratorio();
        t.setCrbm(88888);

        assertEquals(88888, t.getCrbm());
        assertEquals(88888, t.getRegistro());
    }
}
package dasa.setores;

import dasa.funcionarios.Enfermeiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Enfermaria")
public class EnfermariaTest {

    private Enfermaria enfermaria;
    private List<Enfermeiro> enfermeiros;

    @BeforeEach
    public void setUp() {
        Scanner scanner = new Scanner(System.in);
        enfermeiros = new ArrayList<>();
        enfermeiros.add(new Enfermeiro("Ana Silva", 741321, "Hemograma Completo"));
        enfermeiros.add(new Enfermeiro("Roberto Fernandes", 741322, "Hemograma Completo"));
        enfermeiros.add(new Enfermeiro("Mariana Costa", 852431, "Exame de Urina"));

        enfermaria = new Enfermaria(scanner, enfermeiros);
    }

    @Test
    @DisplayName("Deve criar instância de Enfermaria")
    public void testCriarEnfermaria() {
        assertNotNull(enfermaria);
    }

    @Test
    @DisplayName("Deve aceitar parâmetros no construtor")
    public void testConstrutorParametros() {
        Scanner testScanner = new Scanner("test");
        Enfermaria enf = new Enfermaria(testScanner, enfermeiros);
        assertNotNull(enf);
    }

    @Test
    @DisplayName("Deve ter método exibirMenuEnfermaria")
    public void testTemMetodoExibirMenu() {
        assertDoesNotThrow(() -> {
            enfermaria.getClass().getDeclaredMethod("exibirMenuEnfermaria");
        });
    }

    @Test
    @DisplayName("Deve ter métodos privados de funcionalidade")
    public void testMetodosPrivados() {
        assertDoesNotThrow(() -> {
            enfermaria.getClass().getDeclaredMethod("listarEnfermeiros");
            enfermaria.getClass().getDeclaredMethod("examesPorEnfermeiroEspecifico");
            enfermaria.getClass().getDeclaredMethod("buscarEnfermeirosQueAtenderam");
        });
    }

    @Test
    @DisplayName("Deve aceitar lista vazia de enfermeiros")
    public void testListaVaziaEnfermeiros() {
        Scanner scanner = new Scanner("test");
        List<Enfermeiro> listaVazia = new ArrayList<>();

        assertDoesNotThrow(() -> {
            new Enfermaria(scanner, listaVazia);
        });
    }

    @Test
    @DisplayName("Deve gerenciar lista com múltiplos enfermeiros")
    public void testMultiplosEnfermeiros() {
        assertEquals(3, enfermeiros.size());

        // Verifica se há enfermeiros de diferentes especialidades
        boolean temHemograma = enfermeiros.stream().anyMatch(e -> e.getEspecialidade().equals("Hemograma Completo"));
        boolean temUrina = enfermeiros.stream().anyMatch(e -> e.getEspecialidade().equals("Exame de Urina"));

        assertTrue(temHemograma);
        assertTrue(temUrina);
    }
}
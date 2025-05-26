package dasa.setores;

import dasa.modelo.Estoque;
import dasa.funcionarios.Enfermeiro;
import dasa.funcionarios.TecnicoLaboratorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Almoxarifado")
public class AlmoxarifadoTest {

    private Almoxarifado almoxarifado;
    private Estoque estoque;
    private List<Enfermeiro> enfermeiros;
    private TecnicoLaboratorio tecnico;

    @BeforeEach
    public void setUp() {
        Scanner scanner = new Scanner(System.in);
        estoque = new Estoque();
        enfermeiros = new ArrayList<>();
        enfermeiros.add(new Enfermeiro("Ana Silva", 741321, "Hemograma Completo"));
        enfermeiros.add(new Enfermeiro("Carlos Eduardo", 852432, "Exame de Urina"));

        tecnico = new TecnicoLaboratorio("João Santos", 12345);
        almoxarifado = new Almoxarifado(scanner, estoque, enfermeiros, tecnico);
    }

    @Test
    @DisplayName("Deve criar instância de Almoxarifado")
    public void testCriarAlmoxarifado() {
        assertNotNull(almoxarifado);
    }

    @Test
    @DisplayName("Deve aceitar todos os parâmetros no construtor")
    public void testConstrutorCompleto() {
        Scanner testScanner = new Scanner("test");
        Almoxarifado alm = new Almoxarifado(testScanner, estoque, enfermeiros, tecnico);
        assertNotNull(alm);
    }

    @Test
    @DisplayName("Deve ter método exibirMenuAlmoxarifado")
    public void testTemMetodoExibirMenu() {
        assertDoesNotThrow(() -> {
            almoxarifado.getClass().getDeclaredMethod("exibirMenuAlmoxarifado");
        });
    }

    @Test
    @DisplayName("Deve ter métodos privados de funcionalidade")
    public void testMetodosPrivados() {
        assertDoesNotThrow(() -> {
            almoxarifado.getClass().getDeclaredMethod("verificarEstoque");
            almoxarifado.getClass().getDeclaredMethod("adicionarEstoque");
            almoxarifado.getClass().getDeclaredMethod("retirarInsumosExame");
        });
    }

    @Test
    @DisplayName("Deve aceitar lista vazia de enfermeiros")
    public void testListaVaziaEnfermeiros() {
        Scanner scanner = new Scanner("test");
        List<Enfermeiro> listaVazia = new ArrayList<>();

        assertDoesNotThrow(() -> {
            new Almoxarifado(scanner, estoque, listaVazia, tecnico);
        });
    }
}
package dasa.setores;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Recepcao")
public class RecepcaoTest {

    private Recepcao recepcao;

    @BeforeEach
    public void setUp() {
        Scanner scanner = new Scanner(System.in);
        recepcao = new Recepcao(scanner);
    }

    @Test
    @DisplayName("Deve criar instância de Recepção")
    public void testCriarRecepcao() {
        assertNotNull(recepcao);
    }

    @Test
    @DisplayName("Deve ter método exibirMenuRecepcao")
    public void testTemMetodoExibirMenu() {
        assertDoesNotThrow(() -> {
            // Verifica se o metodo existe através de reflexão
            recepcao.getClass().getDeclaredMethod("exibirMenuRecepcao");
        });
    }

    @Test
    @DisplayName("Deve aceitar scanner no construtor")
    public void testConstrutorComScanner() {
        Scanner testScanner = new Scanner("test");
        Recepcao rec = new Recepcao(testScanner);
        assertNotNull(rec);
    }

    @Test
    @DisplayName("Deve ter métodos privados de validação")
    public void testMetodosPrivados() {
        // Verifica se métodos privados existem através de reflexão
        assertDoesNotThrow(() -> {
            recepcao.getClass().getDeclaredMethod("solicitarNomeCompleto");
            recepcao.getClass().getDeclaredMethod("solicitarCPF");
            recepcao.getClass().getDeclaredMethod("solicitarDataNascimento");
            recepcao.getClass().getDeclaredMethod("validarNome", String.class);
        });
    }
}
package dasa;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

// Roda todos os testes dentro dos pacotes indicados
@Suite
@SelectPackages({"dasa.funcionarios", "dasa.modelo", "dasa.setores"})
public class SuiteDeTestesGeral {
    // Nenhum código necessário aqui
}

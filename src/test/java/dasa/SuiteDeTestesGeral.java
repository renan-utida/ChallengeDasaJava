package dasa;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

// Roda todos os testes dentro dos pacotes indicados
@Suite
@SelectPackages({"dasa.config", "dasa.controller"})
public class SuiteDeTestesGeral {
    // Nenhum código necessário aqui
}

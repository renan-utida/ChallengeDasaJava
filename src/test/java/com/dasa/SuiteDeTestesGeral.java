package com.dasa;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

// Roda todos os testes dentro dos pacotes indicados
@Suite
@SelectPackages({"com.dasa.api", "com.dasa.config", "com.dasa.controller", "com.dasa.dto", "com.dasa.model", "com.dasa.service"})
public class SuiteDeTestesGeral {
    // Nenhum código necessário aqui
}

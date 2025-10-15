package com.dasa.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Challenge Dasa Java API")
                        .description("Documentação OpenAPI/Swagger dos recursos (Pacientes, Insumos, Exames).")
                        .version("v1"));
    }
}

package com.odin.odin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI odinOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("ODIN - Gestor Documental API")
                        .version("1.0.0")
                        .description("Contratos REST de los módulos críticos de ODIN: radicados, usuarios, trámites, documentos, notificaciones, firmas y expedientes.")
                        .contact(new Contact().name("Equipo ODIN / SENA")));
    }
}

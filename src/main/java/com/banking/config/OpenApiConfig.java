package com.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion Bancaire")
                        .description("API REST pour la gestion des transactions bancaires, des comptes et des utilisateurs")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Banking API Team")
                                .email("contact@banking-api.com")
                                .url("https://www.banking-api.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement"),
                        new Server()
                                .url("https://your-app.onrender.com")
                                .description("Serveur de production (Render)"),
                        new Server()
                                .url("https://your-app.neon.tech")
                                .description("Serveur de production (Neon)")
                ));
    }
}

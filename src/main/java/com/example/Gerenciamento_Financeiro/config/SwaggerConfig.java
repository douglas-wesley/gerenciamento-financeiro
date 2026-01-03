package com.example.Gerenciamento_Financeiro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gerenciador Financeiro")
                        .version("1.0")
                        .description("API for managing loans and financial transactions")
                        .contact(new Contact()
                                .name("Douglas")
                                .email("douglaswesley0407@gmail.com")
                                .url("https://github.com/douglas-wesley")));
    }

}

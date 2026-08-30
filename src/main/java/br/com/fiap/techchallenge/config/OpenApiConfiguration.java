package br.com.fiap.techchallenge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tech Challenge API",
                version = "v1",
                description = "API para o Tech Challenge FIAP",
                contact = @Contact(
                        name = "Onofre Lima"
                )
        )
)
public class OpenApiConfiguration {
}

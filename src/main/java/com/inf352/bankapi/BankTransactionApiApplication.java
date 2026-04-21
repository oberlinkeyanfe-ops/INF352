package com.inf352.bankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

@SpringBootApplication
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "API-TOKEN")
@OpenAPIDefinition(info = @Info(
        title = "BANK-API",
        version = "1.0.0",
        description = "Specification de l'application BANK-API"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "https://bank-api-oberlin.onrender.com", description = "Production")
        })
public class BankTransactionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankTransactionApiApplication.class, args);
    }
}
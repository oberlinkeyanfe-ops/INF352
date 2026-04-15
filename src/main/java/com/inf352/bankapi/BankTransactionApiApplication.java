package com.inf352.bankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
    title = "BANK-API",
        version = "1.0.0",
    description = "Specification de l'application BANK-API"))
public class BankTransactionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankTransactionApiApplication.class, args);
    }
}
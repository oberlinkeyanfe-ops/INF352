package com.inf352.bankapi.controller;

import com.inf352.bankapi.model.Bank;
import com.inf352.bankapi.service.BankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banks")
@Tag(name = "Banks", description = "Bank management")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<Bank> getBanks() {
        return bankService.getBanks();
    }
}
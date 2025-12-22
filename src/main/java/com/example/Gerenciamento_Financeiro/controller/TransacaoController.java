package com.example.Gerenciamento_Financeiro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Transacoes!";
    }
}

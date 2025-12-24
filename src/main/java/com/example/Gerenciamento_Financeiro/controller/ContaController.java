package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaServices contaServices;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Contas!";
    }

    @PostMapping("/criar")
    public ResponseEntity<ContaResponseDTO> criaConta(@RequestBody ContaRequestDTO dto) {
        ContaResponseDTO novaConta = contaServices.criaConta(dto);
        return ResponseEntity.status(201).body(novaConta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> getContaById(@PathVariable Long id){
        ContaResponseDTO conta = contaServices.getContaById(id);
        return ResponseEntity.ok(conta);
    }

    @DeleteMapping("/{id}") // Criar uma exception para isso
    public ResponseEntity<Void> deleteContaById(@PathVariable Long id){
        contaServices.deleteContaById(id);
        return ResponseEntity.noContent().build();
    }
}

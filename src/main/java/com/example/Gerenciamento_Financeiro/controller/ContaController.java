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


    private final ContaServices services;

    public ContaController(ContaServices services) {
        this.services = services;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Contas!";
    }

    @PostMapping("/criar")
    public ResponseEntity<ContaResponseDTO> criaConta(@RequestBody ContaRequestDTO dto) {
        ContaResponseDTO novaConta = services.criaConta(dto);
        return ResponseEntity.status(201).body(novaConta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> getContaById(@PathVariable Long id){
        ContaResponseDTO conta = services.getContaById(id);
        return ResponseEntity.ok(conta);
    }

    @DeleteMapping("/{id}") // Criar uma exception para isso
    public ResponseEntity<Void> deleteContaById(@PathVariable Long id){
        services.deleteContaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ContaResponseDTO> updateContaById(@PathVariable Long id, @RequestBody ContaRequestDTO dto) {
        ContaResponseDTO contaAtualizada = services.updateContaById(id, dto);
        return ResponseEntity.ok(contaAtualizada);
    }
}

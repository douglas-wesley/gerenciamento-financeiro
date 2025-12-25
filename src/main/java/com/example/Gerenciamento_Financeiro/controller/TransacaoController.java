package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.services.TransacaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoServices services;

    @PostMapping("/criar")
    public ResponseEntity<TransacaoResponseDTO> criaTransacao(@RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = services.criaTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> getTransacaoById(@PathVariable Long id){
        TransacaoResponseDTO dto = services.getTransacaoById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransacaoById(@PathVariable Long id){
        services.deletaTransacao(id);
        return ResponseEntity.noContent().build();
    }

}

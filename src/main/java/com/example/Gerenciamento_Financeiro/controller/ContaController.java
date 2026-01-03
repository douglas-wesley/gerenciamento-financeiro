package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas")
public class ContaController {

    private final ContaServices services;

    public ContaController(ContaServices services) {
        this.services = services;
    }

    // Retirar devido ao login
    @GetMapping("/{id}")
    @Operation(summary = "Obter conta por ID", description = "Retorna os detalhes de uma conta específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    public ResponseEntity<ContaResponseDTO> getContaById(@PathVariable Long id){
        ContaResponseDTO conta = services.getContaById(id);
        return ResponseEntity.ok(conta);
    }

    @DeleteMapping("/{id}") // Criar uma exception para isso
    @Operation(summary = "Deletar conta por ID", description = "Deleta uma conta específica pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    public ResponseEntity<Void> deleteContaById(@PathVariable Long id){
        services.deleteContaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar conta por ID", description = "Atualiza os detalhes de uma conta específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    public ResponseEntity<ContaResponseDTO> updateContaById(@PathVariable Long id, @RequestBody ContaRequestDTO dto) {
        ContaResponseDTO contaAtualizada = services.updateContaById(id, dto);
        return ResponseEntity.ok(contaAtualizada);
    }

    @GetMapping
    @Operation(summary = "Obter todas as contas", description = "Retorna uma lista de todas as contas")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma conta encontrada")
    public ResponseEntity<List<ContaResponseDTO>> getAllContas() {
        List<ContaResponseDTO> contas = services.getAllContas();
        return ResponseEntity.ok(contas);
    }
}

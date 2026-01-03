package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.services.TransacaoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Endpoints para gerenciamento de transações")
public class TransacaoController {


    private final TransacaoServices services;

    public TransacaoController(TransacaoServices services) {
        this.services = services;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar nova transação", description = "Cria uma nova transação financeira")
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    public ResponseEntity<TransacaoResponseDTO> criaTransacao(@RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = services.criaTransacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter transação por ID", description = "Retorna os detalhes de uma transação específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    public ResponseEntity<TransacaoResponseDTO> getTransacaoById(@PathVariable Long id){
        TransacaoResponseDTO dto = services.getTransacaoById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação por ID", description = "Deleta uma transação específica pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    public ResponseEntity<Void> deleteTransacaoById(@PathVariable Long id){
        services.deletaTransacao(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar transação por ID", description = "Atualiza os detalhes de uma transação específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    public ResponseEntity<TransacaoResponseDTO> updateTransacaoById(@PathVariable Long id, @RequestBody TransacaoRequestDTO dto){
        TransacaoResponseDTO updatedDto = services.updateTransacao(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping
    @Operation(summary = "Obter todas as transações", description = "Retorna uma lista de todas as transações")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada")
    public ResponseEntity<List<TransacaoResponseDTO>> getAllTransacoes() {
        List<TransacaoResponseDTO> transacoes = services.getAllTransacoes();
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/conta/{contaId}")
    @Operation(summary = "Obeter todas as transações por ID da conta", description = "Retorna uma lista de todas as transações associadas a uma conta específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada para a conta especificada")
    public ResponseEntity<List<TransacaoResponseDTO>> getAllTransacoesByContaId(@PathVariable Long contaId){
        List<TransacaoResponseDTO> transacoes = services.getAllTransacoesByConta(contaId);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/tipo/{tipoId}")
    @Operation(summary = "Obeter todas as transações por tipo", description = "Retorna uma lista de todas as transações de um tipo específico (DESPESA ou RECEITA)")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada para o tipo especificado")
    public ResponseEntity<List<TransacaoResponseDTO>> getAllTransacoesByTipo(@PathVariable Tipo tipoId){
        List<TransacaoResponseDTO> transacoes = services.getAllTransacoesByTipo(tipoId);
        return ResponseEntity.ok(transacoes);
    }
}

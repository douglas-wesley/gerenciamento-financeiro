package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.services.CategoriaServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
public class CategoriaController {

    private final CategoriaServices services;

    public CategoriaController(CategoriaServices services) {
        this.services = services;
    }

    @PostMapping("/criar")
    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria no sistema")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO novaCategoria = services.criaCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obter categoria por ID", description = "Retorna os detalhes de uma categoria específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<CategoriaResponseDTO> getCategoriaById(@PathVariable  Long id){
        CategoriaResponseDTO categoria = services.getCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria por ID", description = "Deleta uma categoria específica pelo seu ID")
    @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<Void> deleteCategoriaById(@PathVariable Long id){
        services.deleteCategoriaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    @Operation(summary = "Atualizar categoria por ID", description = "Atualiza os detalhes de uma categoria específica pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    public ResponseEntity<CategoriaResponseDTO> updateCategoriaById(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto){
        CategoriaResponseDTO categoriaAtualizada = services.updateCategoriaById(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @GetMapping
    @Operation(summary = "Obter todas as categorias", description = "Retorna uma lista de todas as categorias no sistema")
    @ApiResponse(responseCode = "200", description = "Categorias recuperadas com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhuma categoria encontrada")
    public ResponseEntity<List<CategoriaResponseDTO>> getAllCategorias() {
        List<CategoriaResponseDTO> categorias = services.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }
}

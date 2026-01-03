package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.services.CategoriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaServices services;

    public CategoriaController(CategoriaServices services) {
        this.services = services;
    }

    @PostMapping("/criar")
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO novaCategoria = services.criaCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getCategoriaById(@PathVariable  Long id){
        CategoriaResponseDTO categoria = services.getCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoriaById(@PathVariable Long id){
        services.deleteCategoriaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoriaById(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto){
        CategoriaResponseDTO categoriaAtualizada = services.updateCategoriaById(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> getAllCategorias() {
        List<CategoriaResponseDTO> categorias = services.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }
}

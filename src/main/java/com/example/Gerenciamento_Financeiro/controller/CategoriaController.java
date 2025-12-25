package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.services.CategoriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaServices services;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Categorias!";
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
}

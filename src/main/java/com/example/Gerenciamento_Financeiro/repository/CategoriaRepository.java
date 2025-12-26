package com.example.Gerenciamento_Financeiro.repository;


import com.example.Gerenciamento_Financeiro.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNomeCategoria(String nomeCategoria);
}

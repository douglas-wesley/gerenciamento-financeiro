package com.example.Gerenciamento_Financeiro.services.interfaces;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;

import java.util.List;

public interface ICategoriaServices {
    CategoriaResponseDTO criaCategoria(CategoriaRequestDTO dto);
    CategoriaResponseDTO getCategoriaById(Long id);
    void deleteCategoriaById(Long id);
    CategoriaResponseDTO updateCategoriaById(Long id, CategoriaRequestDTO dto);
    List<CategoriaResponseDTO> getAllCategorias();
}

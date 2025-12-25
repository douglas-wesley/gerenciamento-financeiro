package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import com.example.Gerenciamento_Financeiro.services.interfaces.ICategoriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServices implements ICategoriaServices {

    @Autowired
    private CategoriaRepository repository;

    @Override
    public CategoriaResponseDTO criaCategoria(CategoriaRequestDTO dto) {
        // Definir as regras de negócio básica
        if (dto.getNomeCategoria() == null || dto.getNomeCategoria().isEmpty()) { // Ajeitar no Banco de dados
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
        }

        if (dto.getCor() == null) { // Ajeitar no banco de dados
            throw new IllegalArgumentException("A cor da categoria não pode ser vazia.");
        }

        if (repository.existsByNomeCategoria(dto.getNomeCategoria())){
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setCor(dto.getCor());

        repository.save(categoria);

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getCor()
        );
    }

    @Override
    public CategoriaResponseDTO getCategoriaById(Long id) {
        Categoria categoria = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não existe."));
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getCor()
        );
    }

    @Override
    public void deleteCategoriaById(Long id) {
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Categoria não existe.");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public CategoriaResponseDTO updateCategoriaById(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não existe."));

        if (dto.getCor() == null) {
            throw new IllegalArgumentException("A cor da categoria não pode ser vazia.");
        }

        if (dto.getNomeCategoria() == null || dto.getNomeCategoria().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
        }

        if (repository.existsByNomeCategoria(dto.getNomeCategoria())){
            throw new IllegalArgumentException("Já existe uma categoria com esse nome.");
        }


        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setCor(dto.getCor());
        repository.save(categoria);

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getCor()
        );
    }
}

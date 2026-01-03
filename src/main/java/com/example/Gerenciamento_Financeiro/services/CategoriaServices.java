package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import com.example.Gerenciamento_Financeiro.services.interfaces.ICategoriaServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaServices implements ICategoriaServices {


    private final CategoriaRepository repository;

    public CategoriaServices(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public CategoriaResponseDTO criaCategoria(CategoriaRequestDTO dto) {
        // Definir as regras de negócio básica

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os dados da conta não podem ser nulos.");
        }

        if (dto.getNomeCategoria() == null || dto.getNomeCategoria().isEmpty()) { // Ajeitar no Banco de dados
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O nome da categoria não pode ser vazio.");
        }

        if (dto.getCor() == null) { // Ajeitar no banco de dados
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A cor da categoria não pode ser vazia.");
        }

        if (repository.existsByNomeCategoria(dto.getNomeCategoria())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Já existe uma categoria com esse nome.");
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setCor(dto.getCor());

        repository.save(categoria);

        return toResponseDTO(categoria);
    }

    @Override
    public CategoriaResponseDTO getCategoriaById(Long id) {
        Categoria categoria = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoria não existe."));
        return toResponseDTO(categoria);
    }

    @Override
    @Transactional
    public void deleteCategoriaById(Long id) {
        if (!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Categoria não existe.");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public List<CategoriaResponseDTO> getAllCategorias() {
        List<Categoria> categorias = repository.findAll();

        if (categorias.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma categoria encontrada.");
        }

        return categorias.stream()
                .map(this::toResponseDTO)
                .toList();

    }

    @Override
    @Transactional
    public CategoriaResponseDTO updateCategoriaById(Long id, CategoriaRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Os dados da categoria não podem ser nulos.");
        }

        Categoria categoria = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não existe."));

        if (dto.getCor() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A cor da categoria não pode ser vazia.");
        }

        if (dto.getNomeCategoria() == null || dto.getNomeCategoria().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O nome da categoria não pode ser vazio.");
        }

        if (repository.existsByNomeCategoria(dto.getNomeCategoria())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Já existe uma categoria com esse nome.");
        }


        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setCor(dto.getCor());
        repository.save(categoria);

        return toResponseDTO(categoria);
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getCor()
        );
    }
}

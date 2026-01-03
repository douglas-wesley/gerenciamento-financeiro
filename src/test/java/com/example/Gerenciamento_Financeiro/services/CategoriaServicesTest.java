package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServicesTest {

    @InjectMocks
    CategoriaServices service;

    @Mock
    CategoriaRepository repository;

    Categoria categoria;

    @BeforeEach
    public void setUp(){
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNomeCategoria("Transporte");
        categoria.setCor(Cor.AMARELO);
    }

    @Test
    void criarCategoriaComSucesso(){
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("Lazer");
        dto.setCor(Cor.AZUL);

        when(repository.existsByNomeCategoria("Lazer")).thenReturn(false);
        when(repository.save(any(Categoria.class))).thenAnswer(inv -> {
            Categoria c = inv.getArgument(0);
            c.setId(2L);
            return c;
        });

        CategoriaResponseDTO resp = service.criaCategoria(dto);

        assertEquals(2L, resp.getId());
        assertEquals("Lazer", resp.getNomeCategoria());
        assertEquals(Cor.AZUL, resp.getCor());

        verify(repository).existsByNomeCategoria("Lazer");
        verify(repository).save(any(Categoria.class));
    }

    @Test
    void criarCategoriaNomeJaExiste() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("Transporte");
        dto.setCor(Cor.VERDE);

        when(repository.existsByNomeCategoria("Transporte")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criaCategoria(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Já existe uma categoria com esse nome"));

        verify(repository).existsByNomeCategoria("Transporte");
        verify(repository, never()).save(any());
    }

    @Test
    void criarCategoriaDtoNulo() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criaCategoria(null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("não podem ser nulos"));

        verify(repository, never()).save(any());
    }

    @Test
    void criarCategoriaNomeVazio() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("");
        dto.setCor(Cor.AZUL);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criaCategoria(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("nome da categoria não pode ser vazio"));

        verify(repository, never()).save(any());
    }

    @Test
    void criarCategoriaCorNula() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("Lazer");
        dto.setCor(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.criaCategoria(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("cor da categoria não pode ser vazia"));

        verify(repository, never()).save(any());
    }

    @Test
    void getCategoriaByIdComSucesso(){
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaResponseDTO categoriaRetornada = service.getCategoriaById(1L);

        assertEquals(categoria.getId(), categoriaRetornada.getId());
        assertEquals(categoria.getNomeCategoria(), categoriaRetornada.getNomeCategoria());
        assertEquals(categoria.getCor(), categoriaRetornada.getCor());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getCategoriaByIdNaoEncontrada() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.getCategoriaById(2L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Categoria não existe"));

        verify(repository).findById(2L);
    }

    @Test
    void deleteCategoriaByIdSucesso() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteCategoriaById(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteCategoriaByIdNaoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.deleteCategoriaById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Categoria não existe"));

        verify(repository).existsById(99L);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void updateCategoriaComSucesso() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("Transporte Atualizado");
        dto.setCor(Cor.VERMELHO);

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.existsByNomeCategoria("Transporte Atualizado")).thenReturn(false);
        when(repository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoriaResponseDTO resp = service.updateCategoriaById(1L, dto);

        assertEquals(1L, resp.getId());
        assertEquals("Transporte Atualizado", resp.getNomeCategoria());
        assertEquals(Cor.VERMELHO, resp.getCor());

        verify(repository).findById(1L);
        verify(repository).existsByNomeCategoria("Transporte Atualizado");
        verify(repository).save(any(Categoria.class));
    }

    @Test
    void updateCategoriaNomeJaExiste() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("OutroNome");
        dto.setCor(Cor.VERMELHO);

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.existsByNomeCategoria("OutroNome")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.updateCategoriaById(1L, dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Já existe uma categoria com esse nome"));

        verify(repository).findById(1L);
        verify(repository).existsByNomeCategoria("OutroNome");
        verify(repository, never()).save(any());
    }

    @Test
    void updateCategoriaNaoEncontrada() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("NomeQualquer");
        dto.setCor(Cor.VERMELHO);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        // O serviço lança IllegalArgumentException, não ResponseStatusException
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> service.updateCategoriaById(99L, dto));
        assertTrue(ex.getMessage().contains("Categoria não existe"));

        verify(repository).findById(99L);
        verify(repository, never()).existsByNomeCategoria(any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateCategoriaDtoNulo() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.updateCategoriaById(1L, null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("não podem ser nulos"));

        verify(repository, never()).save(any());
    }

    @Test
    void updateCategoriaCorNula() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("NovoNome");
        dto.setCor(null);

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.updateCategoriaById(1L, dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("cor da categoria não pode ser vazia"));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void updateCategoriaNomeVazio() {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNomeCategoria("");
        dto.setCor(Cor.VERMELHO);

        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
            () -> service.updateCategoriaById(1L, dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("nome da categoria não pode ser vazio"));

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }
}

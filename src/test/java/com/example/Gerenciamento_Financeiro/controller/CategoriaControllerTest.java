package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.CategoriaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.CategoriaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import com.example.Gerenciamento_Financeiro.services.CategoriaServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    CategoriaServices services;

    @InjectMocks
    CategoriaController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private CategoriaResponseDTO categoria1;
    private CategoriaResponseDTO categoria2;
    private CategoriaResponseDTO categoria3;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        categoria1 = new CategoriaResponseDTO();
        categoria1.setId(1L);
        categoria1.setNomeCategoria("Transporte");
        categoria1.setCor(Cor.AMARELO);

        categoria2 = new CategoriaResponseDTO();
        categoria2.setId(2L);
        categoria2.setNomeCategoria("Comida");
        categoria2.setCor(Cor.AZUL);

        categoria3 = new CategoriaResponseDTO();
        categoria3.setId(3L);
        categoria3.setNomeCategoria("Lazer");
        categoria3.setCor(Cor.VERDE);
    }

    @Test
    void criarCategoriaComSucesso() throws Exception {
        CategoriaRequestDTO requestDto = new CategoriaRequestDTO();
        requestDto.setNomeCategoria("Transporte");
        requestDto.setCor(Cor.AMARELO);

        when(services.criaCategoria(any(CategoriaRequestDTO.class))).thenReturn(categoria1);

        mockMvc.perform(post("/categorias/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCategoria").value("Transporte"))
                .andExpect(jsonPath("$.cor").value("AMARELO"));

        verify(services).criaCategoria(any(CategoriaRequestDTO.class));
    }

    @Test
    void getAllCategoriasComSucesso() throws Exception {
        when(services.getAllCategorias()).thenReturn(List.of(categoria1, categoria2, categoria3));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].nomeCategoria").value("Comida"));

        verify(services).getAllCategorias();
    }


    @Test
    void getCategoriaByIdComSucesso() throws Exception {
        when(services.getCategoriaById(1L)).thenReturn(categoria1);

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCategoria").value("Transporte"));

        verify(services).getCategoriaById(1L);
    }

    @Test
    void deleteCategoriaByIdComSucesso() throws Exception {
        doNothing().when(services).deleteCategoriaById(1L);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());

        verify(services).deleteCategoriaById(1L);
    }

    @Test
    void updateCategoriaByIdComSucesso() throws Exception {
        CategoriaRequestDTO updateDto = new CategoriaRequestDTO();
        updateDto.setNomeCategoria("Transporte Atualizado");
        updateDto.setCor(Cor.AMARELO);

        when(services.updateCategoriaById(eq(1L), any(CategoriaRequestDTO.class))).thenReturn(categoria1);

        mockMvc.perform(put("/categorias/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(services).updateCategoriaById(eq(1L), any(CategoriaRequestDTO.class));
    }




}

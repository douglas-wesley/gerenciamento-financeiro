package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.*;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ContaControllerTest {

    @Mock
    ContaServices services;

    @InjectMocks
    ContaController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private ContaResponseDTO conta1;
    private ContaResponseDTO conta2;
    private ContaResponseDTO conta3;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        conta1 = new ContaResponseDTO();
        conta1.setId(1L);
        conta1.setNomeTitular("Conta Corrente");
        conta1.setSaldo(BigDecimal.valueOf(1000.0));

        conta2 = new ContaResponseDTO();
        conta2.setId(2L);
        conta2.setNomeTitular("Poupança");
        conta2.setSaldo(BigDecimal.valueOf(5000.0));

        conta3 = new ContaResponseDTO();
        conta3.setId(3L);
        conta3.setNomeTitular("Cartão de Crédito");
        conta3.setSaldo(BigDecimal.valueOf(-2000.0));
    }

    @Test
    void getContaByIdComSucesso() throws Exception {
        when(services.getContaById(1L)).thenReturn(conta1);

        mockMvc.perform(get("/contas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeTitular").value("Conta Corrente"))
                .andExpect(jsonPath("$.saldo").value(1000.0));

        verify(services).getContaById(1L);
    }

    @Test
    void getContaByIdNotFound() throws Exception {
        when(services.getContaById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        mockMvc.perform(get("/contas/99"))
                .andExpect(status().isNotFound());

        verify(services).getContaById(99L);
    }

    @Test
    void deleteContaByIdComSucesso() throws Exception {
        doNothing().when(services).deleteContaById(2L);

        mockMvc.perform(delete("/contas/2"))
               .andExpect(status().isNoContent());

        verify(services).deleteContaById(2L);
    }

    @Test
    void deleteContaByIdNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"))
                .when(services).deleteContaById(5L);

        mockMvc.perform(delete("/contas/5"))
                .andExpect(status().isNotFound());

        verify(services).deleteContaById(5L);
    }

    @Test
    void getAllContaComSucesso() throws  Exception {
        when(services.getAllContas()).thenReturn(List.of(conta1, conta2, conta3));

        mockMvc.perform(get("/contas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));

        verify(services).getAllContas();
    }

    @Test
    void getAllContaEmpty() throws Exception {
        when(services.getAllContas())
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma conta encontrada"));

        mockMvc.perform(get("/contas"))
                .andExpect(status().isNotFound());

        verify(services).getAllContas();
    }

    @Test
    void updateContaByIdComSucesso() throws Exception {
        ContaRequestDTO request = new ContaRequestDTO();
        request.setNomeTitular("Conta Atualizada");
        request.setSaldo(BigDecimal.valueOf(1500.0));

        ContaResponseDTO updated = new ContaResponseDTO();
        updated.setId(1L);
        updated.setNomeTitular("Conta Atualizada");
        updated.setSaldo(BigDecimal.valueOf(1500.0));

        when(services.updateContaById(eq(1L), any(ContaRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/contas/atualizar/1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeTitular").value("Conta Atualizada"))
                .andExpect(jsonPath("$.saldo").value(1500.0));

        verify(services).updateContaById(eq(1L), any(ContaRequestDTO.class));
    }
}

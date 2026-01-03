package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.services.TransacaoServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoControllerTest {

    @Mock
    TransacaoServices services;

    @InjectMocks
    TransacaoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TransacaoResponseDTO transacao1;
    private TransacaoResponseDTO transacao2;
    private TransacaoResponseDTO transacao3;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        transacao1 = new TransacaoResponseDTO();
        transacao1.setId(1L);
        transacao1.setDescricao("Salário");
        transacao1.setValor(BigDecimal.valueOf(5000.00));
        transacao1.setData(LocalDate.now());
        transacao1.setTipo("RECEITA");
        transacao1.setContaId(1L);
        transacao1.setCategoriasIds(List.of(10L, 11L));

        transacao2 = new TransacaoResponseDTO();
        transacao2.setId(2L);
        transacao2.setDescricao("Aluguel");
        transacao2.setValor(BigDecimal.valueOf(1500.00));
        transacao2.setData(LocalDate.now());
        transacao2.setTipo("DESPESA");
        transacao2.setContaId(1L);
        transacao2.setCategoriasIds(List.of(12L));

        transacao3 = new TransacaoResponseDTO();
        transacao3.setId(3L);
        transacao3.setDescricao("Freelance");
        transacao3.setValor(BigDecimal.valueOf(800.00));
        transacao3.setData(LocalDate.now().minusDays(5));
        transacao3.setTipo("RECEITA");
        transacao3.setContaId(1L);
        transacao3.setCategoriasIds(List.of());
    }

    @Test
    void criarTransacaoComSucesso() throws Exception {
        TransacaoRequestDTO requestDto = new TransacaoRequestDTO();
        requestDto.setDescricao("Salário");
        requestDto.setValor(BigDecimal.valueOf(5000.00));
        requestDto.setData(LocalDate.now());
        requestDto.setTipo(Tipo.RECEITA);
        requestDto.setContaId(1L);
        requestDto.setCategoriasIds(List.of(10L, 11L));

        when(services.criaTransacao(any(TransacaoRequestDTO.class))).thenReturn(transacao1);

        mockMvc.perform(post("/transacoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Salário"))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.valor").value(5000.00));

        verify(services).criaTransacao(any(TransacaoRequestDTO.class));
    }

    @Test
    void criarTransacaoSaldoInsuficiente() throws Exception {
        TransacaoRequestDTO requestDto = new TransacaoRequestDTO();
        requestDto.setDescricao("Compra");
        requestDto.setValor(BigDecimal.valueOf(10000.00));
        requestDto.setData(LocalDate.now());
        requestDto.setTipo(Tipo.DESPESA);
        requestDto.setContaId(1L);

        when(services.criaTransacao(any(TransacaoRequestDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente"));

        mockMvc.perform(post("/transacoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(services).criaTransacao(any(TransacaoRequestDTO.class));
    }

    @Test
    void getTransacaoByIdComSucesso() throws Exception {
        when(services.getTransacaoById(1L)).thenReturn(transacao1);

        mockMvc.perform(get("/transacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Salário"))
                .andExpect(jsonPath("$.tipo").value("RECEITA"));

        verify(services).getTransacaoById(1L);
    }

    @Test
    void getTransacaoByIdNotFound() throws Exception {
        when(services.getTransacaoById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        mockMvc.perform(get("/transacoes/99"))
                .andExpect(status().isNotFound());

        verify(services).getTransacaoById(99L);
    }

    @Test
    void getAllTransacoesComSucesso() throws Exception {
        when(services.getAllTransacoes()).thenReturn(List.of(transacao1, transacao2, transacao3));

        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));

        verify(services).getAllTransacoes();
    }

    @Test
    void getAllTransacoesEmpty() throws Exception {
        when(services.getAllTransacoes())
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma transação encontrada"));

        mockMvc.perform(get("/transacoes"))
                .andExpect(status().isNotFound());

        verify(services).getAllTransacoes();
    }

    @Test
    void deleteTransacaoByIdComSucesso() throws Exception {
        doNothing().when(services).deletaTransacao(1L);

        mockMvc.perform(delete("/transacoes/1"))
                .andExpect(status().isNoContent());

        verify(services).deletaTransacao(1L);
    }

    @Test
    void deleteTransacaoByIdNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"))
                .when(services).deletaTransacao(99L);

        mockMvc.perform(delete("/transacoes/99"))
                .andExpect(status().isNotFound());

        verify(services).deletaTransacao(99L);
    }

    @Test
    void deleteTransacaoMaisDeSeisMeses() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transação com mais de 6 meses não pode ser deletada"))
                .when(services).deletaTransacao(5L);

        mockMvc.perform(delete("/transacoes/5"))
                .andExpect(status().isBadRequest());

        verify(services).deletaTransacao(5L);
    }

    @Test
    void updateTransacaoByIdComSucesso() throws Exception {
        TransacaoRequestDTO requestDto = new TransacaoRequestDTO();
        requestDto.setDescricao("Salário Atualizado");
        requestDto.setValor(BigDecimal.valueOf(5500.00));
        requestDto.setData(LocalDate.now());
        requestDto.setTipo(Tipo.RECEITA);
        requestDto.setContaId(1L);
        requestDto.setCategoriasIds(List.of(10L));

        TransacaoResponseDTO updated = new TransacaoResponseDTO();
        updated.setId(1L);
        updated.setDescricao("Salário Atualizado");
        updated.setValor(BigDecimal.valueOf(5500.00));
        updated.setData(LocalDate.now());
        updated.setTipo("RECEITA");
        updated.setContaId(1L);
        updated.setCategoriasIds(List.of(10L));

        when(services.updateTransacao(eq(1L), any(TransacaoRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/transacoes/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Salário Atualizado"))
                .andExpect(jsonPath("$.valor").value(5500.00));

        verify(services).updateTransacao(eq(1L), any(TransacaoRequestDTO.class));
    }

    @Test
    void updateTransacaoByIdNotFound() throws Exception {
        TransacaoRequestDTO requestDto = new TransacaoRequestDTO();
        requestDto.setDescricao("Atualizada");
        requestDto.setValor(BigDecimal.valueOf(100.00));
        requestDto.setData(LocalDate.now());
        requestDto.setTipo(Tipo.RECEITA);
        requestDto.setContaId(1L);

        when(services.updateTransacao(eq(99L), any(TransacaoRequestDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        mockMvc.perform(put("/transacoes/atualizar/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());

        verify(services).updateTransacao(eq(99L), any(TransacaoRequestDTO.class));
    }
}

package com.example.Gerenciamento_Financeiro.services;


import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransacaoServicesTest {
    @InjectMocks
    TransacaoServices services;

    @Mock
    TransacaoRepository repository;

    @Mock
    ContaRepository contaRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    Conta conta;
    Categoria cat1;
    Categoria cat2;

    @BeforeEach
    void setup(){
        conta = new Conta();
        conta.setId(1L);
        conta.setSaldo(BigDecimal.valueOf(1000.00));

        cat1 = new Categoria();
        cat1.setId(10L);
        cat2 = new Categoria();
        cat2.setId(11L);
    }


    @Test
    void criaTransacaoComSucesso_receitaComCategorias() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setContaId(1L);
        dto.setValor(BigDecimal.valueOf(200.00));
        dto.setData(LocalDate.now());
        dto.setTipo(Tipo.RECEITA);
        dto.setDescricao("Salário");
        dto.setCategoriasIds(List.of(10L, 11L));

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findAllById(dto.getCategoriasIds())).thenReturn(List.of(cat1, cat2));
        when(repository.save(any(Transacao.class))).thenAnswer(inv -> {
            Transacao t = inv.getArgument(0);
            t.setId(100L);
            return t;
        });

        TransacaoResponseDTO resp = services.criaTransacao(dto);

        assertNotNull(resp);
        assertEquals(100L, resp.getId());
        assertEquals("RECEITA", resp.getTipo());
        assertEquals(1L, resp.getContaId());
        assertEquals(0, BigDecimal.valueOf(1200.00).compareTo(conta.getSaldo())); // saldo atualizado
        verify(contaRepository).save(conta);
        verify(repository).save(any(Transacao.class));
    }

    @Test
    void criaTransacaoContaNaoEncontrada() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setContaId(99L);
        dto.setValor(BigDecimal.valueOf(10));
        dto.setData(LocalDate.now());
        dto.setTipo(Tipo.RECEITA);

        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.criaTransacao(dto));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void criaTransacaoSaldoInsuficienteDespesa() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setContaId(1L);
        dto.setValor(BigDecimal.valueOf(2000.00));
        dto.setData(LocalDate.now());
        dto.setTipo(Tipo.DESPESA);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.criaTransacao(dto));
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void criaTransacaoCategoriasFaltando() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setContaId(1L);
        dto.setValor(BigDecimal.valueOf(50));
        dto.setData(LocalDate.now());
        dto.setTipo(Tipo.RECEITA);
        dto.setCategoriasIds(List.of(10L, 99L)); // 99L ausente

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(categoriaRepository.findAllById(dto.getCategoriasIds())).thenReturn(List.of(cat1)); // retorna só 10L

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.criaTransacao(dto));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Categorias"));
    }

    @Test
    void deletaTransacaoMaisDeSeisMesesNaoPermite() {
        Transacao t = new Transacao();
        t.setId(200L);
        t.setData(LocalDate.now().minusMonths(7));
        t.setValor(BigDecimal.valueOf(10));
        t.setTipo(Tipo.DESPESA);
        t.setConta(conta);

        when(repository.findById(200L)).thenReturn(Optional.of(t));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.deletaTransacao(200L));
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deletaTransacaoComSucessoReverteSaldo() {
        Transacao t = new Transacao();
        t.setId(201L);
        t.setData(LocalDate.now());
        t.setValor(BigDecimal.valueOf(100));
        t.setTipo(Tipo.DESPESA);
        t.setConta(conta);

        when(repository.findById(201L)).thenReturn(Optional.of(t));

        services.deletaTransacao(201L);

        // Para DESPESA, o saldo deve aumentar ao reverter
        assertEquals(0, BigDecimal.valueOf(1100.00).compareTo(conta.getSaldo()));
        verify(contaRepository).save(conta);
        verify(repository).deleteById(201L);
    }

    @Test
    void updateTransacaoComSucesso_alteraValoresECategorias() {
        Transacao existente = new Transacao();
        existente.setId(300L);
        existente.setData(LocalDate.now().minusDays(1));
        existente.setValor(BigDecimal.valueOf(100));
        existente.setTipo(Tipo.DESPESA);
        existente.setConta(conta);
        // inicializa a lista de categorias para evitar NPE
        existente.setCategorias(new ArrayList<>());

        TransacaoRequestDTO dto = new TransacaoRequestDTO();
        dto.setData(LocalDate.now());
        dto.setValor(BigDecimal.valueOf(50));
        dto.setTipo(Tipo.DESPESA);
        dto.setDescricao("Atualizada");
        dto.setCategoriasIds(List.of(10L));

        when(repository.findById(300L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findAllById(dto.getCategoriasIds())).thenReturn(List.of(cat1));
        when(repository.save(any(Transacao.class))).thenAnswer(inv -> inv.getArgument(0));
        when(contaRepository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

        var resp = services.updateTransacao(300L, dto);

        assertNotNull(resp);
        assertEquals("Atualizada", resp.getDescricao());
        assertEquals("DESPESA", resp.getTipo());
        // saldo: inicialmente 1000.00, reverte antigo DESPESA (+100) => 1100, aplica nova DESPESA (-50) => 1050
        assertEquals(0, BigDecimal.valueOf(1050.00).compareTo(conta.getSaldo()));
        verify(repository).save(any(Transacao.class));
        verify(contaRepository).save(conta);
    }

}

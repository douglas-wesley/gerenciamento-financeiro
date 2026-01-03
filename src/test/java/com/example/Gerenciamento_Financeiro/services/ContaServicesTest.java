package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServicesTest {

    @Mock
    ContaRepository repository;

    @InjectMocks
    ContaServices services;

    @Mock
    PasswordEncoder passwordEncoder;

    Conta conta1;
    Conta conta2;

    @BeforeEach
    void setup() throws Exception {
        conta1 = new Conta();
        conta1.setId(1L);
        conta1.setNomeTitular("Douglas");
        conta1.setSaldo(BigDecimal.valueOf(1000.0));
        conta1.setNomeBanco("Nubank");
        conta1.setNumeroConta(123456);
        conta1.setEmail("douglas@gmail.com");
        conta1.setSenha("senha123");

        conta2 = new Conta();
        conta2.setId(2L);
        conta2.setNomeTitular("Adrian");
        conta2.setSaldo(BigDecimal.valueOf(5000.0));
        conta2.setNomeBanco("Nubank");
        conta2.setNumeroConta(654321);
        conta2.setEmail("adrian@gmail.com");
        conta2.setSenha("senha456");
    }

    @Test
    void criaContaComSucesso(){
        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setNomeTitular("Douglas");
        dto.setSaldo(BigDecimal.valueOf(2000.0));
        dto.setNomeBanco("Nubank");
        dto.setNumeroConta(123456);
        dto.setEmail("douglas@gmail.com");
        dto.setSenha("senha123");

        when(repository.existsByNumeroConta(dto.getNumeroConta())).thenReturn(false);
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedSenha");
        when(repository.save(any(Conta.class))).thenAnswer(inv -> {
            Conta c = inv.getArgument(0);
            c.setId(3L);
            return c;
        });

        ContaResponseDTO resp = services.criaConta(dto);

        assertNotNull(resp);
        assertEquals(3L, resp.getId());
        assertEquals("Douglas", resp.getNomeTitular());
        assertEquals(0, BigDecimal.valueOf(2000.0).compareTo(resp.getSaldo()));

        verify(repository).existsByNumeroConta(123456);
        verify(repository).existsByEmail("douglas@gmail.com");
        verify(passwordEncoder).encode("senha123");
        verify(repository).save(any(Conta.class));
    }

    @Test
    void criaContaNumeroContaJaExiste() {
        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setNomeTitular("Douglas");
        dto.setSaldo(BigDecimal.valueOf(2000.0));
        dto.setNomeBanco("Nubank");
        dto.setNumeroConta(1);
        dto.setEmail("x@example.com");
        dto.setSenha("senhaSegura");

        when(repository.existsByNumeroConta(1)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.criaConta(dto));
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().toLowerCase().contains("número da conta"));
    }


    @Test
    void getContaByIdComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(conta1));

        ContaResponseDTO resp = services.getContaById(1L);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Douglas", resp.getNomeTitular());
        verify(repository).findById(1L);
    }

    @Test
    void getAllContasComSucesso() {
        when(repository.findAll()).thenReturn(List.of(conta1, conta2));

        var lista = services.getAllContas();

        assertNotNull(lista);
        assertEquals(2, lista.size());
        verify(repository).findAll();
    }

    @Test
    void deleteContaByIdNotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> services.deleteContaById(99L));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
        verify(repository).existsById(99L);
    }

    @Test
    void updateContaByIdComSucesso() {
        ContaRequestDTO dto = new ContaRequestDTO();
        dto.setNomeBanco("Banco A Atualizado");
        dto.setNomeTitular("Conta Corrente Atualizada");
        dto.setNumeroConta(conta1.getNumeroConta()); // mantendo o mesmo número
        dto.setSaldo(BigDecimal.valueOf(1500.0));
        dto.setEmail(conta1.getEmail());
        dto.setSenha("novaSenha");

        when(repository.findById(1L)).thenReturn(Optional.of(conta1));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedNova");
        when(repository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

        var updated = services.updateContaById(1L, dto);

        assertNotNull(updated);
        assertEquals("Conta Corrente Atualizada", updated.getNomeTitular());
        assertEquals(0, BigDecimal.valueOf(1500.0).compareTo(updated.getSaldo()));
        verify(repository).findById(1L);
        verify(repository).save(any(Conta.class));
        verify(passwordEncoder).encode("novaSenha");
    }
}

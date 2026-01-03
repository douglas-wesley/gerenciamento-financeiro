package com.example.Gerenciamento_Financeiro.integration;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FluxoGerenciamentoFinanceiroIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Limpar dados antes de cada teste
        transacaoRepository.deleteAll();
        contaRepository.deleteAll();
    }

    @Test
    void fluxoCompleto_registroLoginETransacoes() throws Exception {
        // 1. Registrar nova conta
        ContaRequestDTO contaDto = new ContaRequestDTO();
        contaDto.setNomeTitular("Usuario Teste");
        contaDto.setEmail("teste@integration.com");
        contaDto.setSenha("senha123");
        contaDto.setNomeBanco("Banco Teste");
        contaDto.setNumeroConta(111111);
        contaDto.setSaldo(BigDecimal.valueOf(10000.0));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("teste@integration.com"));

        // 2. Login com a conta criada
        LoginRequestDTO loginDto = new LoginRequestDTO();
        loginDto.setEmail("teste@integration.com");
        loginDto.setPassword("senha123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.email").value("teste@integration.com"));
    }

    @Test
    void fluxoCompleto_criarEListarTransacoes() throws Exception {
        // Primeiro criar uma conta
        ContaRequestDTO contaDto = new ContaRequestDTO();
        contaDto.setNomeTitular("Usuario Teste");
        contaDto.setEmail("teste2@integration.com");
        contaDto.setSenha("senha123");
        contaDto.setNomeBanco("Banco Teste");
        contaDto.setNumeroConta(222222);
        contaDto.setSaldo(BigDecimal.valueOf(10000.0));

        String contaResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair ID da conta criada
        Long contaId = objectMapper.readTree(contaResponse).get("id").asLong();

        // Criar transação de receita com autenticação básica
        TransacaoRequestDTO receitaDto = new TransacaoRequestDTO();
        receitaDto.setDescricao("Salário");
        receitaDto.setValor(BigDecimal.valueOf(5000.0));
        receitaDto.setData(LocalDate.now());
        receitaDto.setTipo(Tipo.RECEITA);
        receitaDto.setContaId(contaId);
        receitaDto.setCategoriasIds(List.of());

        mockMvc.perform(post("/transacoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receitaDto))
                        .header("Authorization", "Basic dGVzdGUyQGludGVncmF0aW9uLmNvbTpzZW5oYTEyMw==")) // teste2@integration.com:senha123
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Salário"))
                .andExpect(jsonPath("$.tipo").value("RECEITA"));

        // Criar transação de despesa
        TransacaoRequestDTO despesaDto = new TransacaoRequestDTO();
        despesaDto.setDescricao("Aluguel");
        despesaDto.setValor(BigDecimal.valueOf(1500.0));
        despesaDto.setData(LocalDate.now());
        despesaDto.setTipo(Tipo.DESPESA);
        despesaDto.setContaId(contaId);
        despesaDto.setCategoriasIds(List.of());

        mockMvc.perform(post("/transacoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(despesaDto))
                        .header("Authorization", "Basic dGVzdGUyQGludGVncmF0aW9uLmNvbTpzZW5oYTEyMw=="))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Aluguel"))
                .andExpect(jsonPath("$.tipo").value("DESPESA"));

        // Listar todas as transações
        mockMvc.perform(get("/transacoes")
                        .header("Authorization", "Basic dGVzdGUyQGludGVncmF0aW9uLmNvbTpzZW5oYTEyMw=="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void fluxoCompleto_validacaoSaldoInsuficiente() throws Exception {
        // Criar conta com saldo baixo
        ContaRequestDTO contaDto = new ContaRequestDTO();
        contaDto.setNomeTitular("Usuario Teste");
        contaDto.setEmail("teste3@integration.com");
        contaDto.setSenha("senha123");
        contaDto.setNomeBanco("Banco Teste");
        contaDto.setNumeroConta(333333);
        contaDto.setSaldo(BigDecimal.valueOf(100.0));

        String contaResponse = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long contaId = objectMapper.readTree(contaResponse).get("id").asLong();

        // Tentar criar despesa maior que o saldo
        TransacaoRequestDTO despesaDto = new TransacaoRequestDTO();
        despesaDto.setDescricao("Compra Grande");
        despesaDto.setValor(BigDecimal.valueOf(1000.0));
        despesaDto.setData(LocalDate.now());
        despesaDto.setTipo(Tipo.DESPESA);
        despesaDto.setContaId(contaId);
        despesaDto.setCategoriasIds(List.of());

        mockMvc.perform(post("/transacoes/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(despesaDto))
                        .header("Authorization", "Basic dGVzdGUzQGludGVncmF0aW9uLmNvbTpzZW5oYTEyMw==")) // teste3@integration.com:senha123
                .andExpect(status().isBadRequest());
    }
}


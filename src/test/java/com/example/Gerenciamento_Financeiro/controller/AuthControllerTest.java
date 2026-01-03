package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginRequestDTO;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    ContaServices contaServices;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerComSucesso() throws Exception {
        ContaRequestDTO requestDto = new ContaRequestDTO();
        requestDto.setNomeTitular("Douglas");
        requestDto.setEmail("douglas@gmail.com");
        requestDto.setSenha("senha123");
        requestDto.setNomeBanco("Nubank");
        requestDto.setNumeroConta(123456);
        requestDto.setSaldo(BigDecimal.valueOf(1000.0));

        ContaResponseDTO responseDto = new ContaResponseDTO();
        responseDto.setId(1L);
        responseDto.setNomeTitular("Douglas");
        responseDto.setEmail("douglas@gmail.com");
        responseDto.setSaldo(BigDecimal.valueOf(1000.0));

        when(contaServices.criaConta(any(ContaRequestDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeTitular").value("Douglas"))
                .andExpect(jsonPath("$.email").value("douglas@gmail.com"));

        verify(contaServices).criaConta(any(ContaRequestDTO.class));
    }

    @Test
    void registerEmailJaExiste() throws Exception {
        ContaRequestDTO requestDto = new ContaRequestDTO();
        requestDto.setNomeTitular("Douglas");
        requestDto.setEmail("douglas@gmail.com");
        requestDto.setSenha("senha123");
        requestDto.setNomeBanco("Nubank");
        requestDto.setNumeroConta(123456);
        requestDto.setSaldo(BigDecimal.valueOf(1000.0));

        when(contaServices.criaConta(any(ContaRequestDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(contaServices).criaConta(any(ContaRequestDTO.class));
    }

    @Test
    void loginComSucesso() throws Exception {
        LoginRequestDTO loginDto = new LoginRequestDTO();
        loginDto.setEmail("douglas@gmail.com");
        loginDto.setPassword("senha123");

        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso"))
                .andExpect(jsonPath("$.email").value("douglas@gmail.com"))
                .andExpect(jsonPath("$.authenticated").value(true));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginCredenciaisInvalidas() throws Exception {
        LoginRequestDTO loginDto = new LoginRequestDTO();
        loginDto.setEmail("douglas@gmail.com");
        loginDto.setPassword("senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou senha inválidos"))
                .andExpect(jsonPath("$.authenticated").value(false));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginEmailNaoEncontrado() throws Exception {
        LoginRequestDTO loginDto = new LoginRequestDTO();
        loginDto.setEmail("naoexiste@gmail.com");
        loginDto.setPassword("senha123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Usuário não encontrado"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou senha inválidos"))
                .andExpect(jsonPath("$.authenticated").value(false));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}


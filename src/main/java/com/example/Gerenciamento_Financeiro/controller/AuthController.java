package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginResponseDTO;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ContaServices contaServices;
    private final AuthenticationManager authenticationManager;


    public AuthController(ContaServices contaServices, AuthenticationManager authenticationManager) {
        this.contaServices = contaServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<ContaResponseDTO> register(@RequestBody ContaRequestDTO dto){
        ContaResponseDTO response = contaServices.criaConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        try {
            // Tenta autenticar usando o email e senha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );

            // Se chegou aqui, a autenticação foi bem-sucedida
            return ResponseEntity.ok(new LoginResponseDTO(
                    "Login realizado com sucesso",
                    dto.getEmail(),
                    true
            ));
        } catch (BadCredentialsException e) {
            // Credenciais inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(
                            "Email ou senha inválidos",
                            null,
                            false
                    ));
        }
    }
}

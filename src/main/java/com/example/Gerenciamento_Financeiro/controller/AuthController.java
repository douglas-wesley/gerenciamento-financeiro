package com.example.Gerenciamento_Financeiro.controller;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.LoginResponseDTO;
import com.example.Gerenciamento_Financeiro.services.ContaServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final ContaServices contaServices;
    private final AuthenticationManager authenticationManager;


    public AuthController(ContaServices contaServices, AuthenticationManager authenticationManager) {
        this.contaServices = contaServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nova conta", description = "Cria uma nova conta de usuário no sistema")
    @ApiResponse(responseCode = "201", description = "Conta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    public ResponseEntity<ContaResponseDTO> register(@RequestBody ContaRequestDTO dto){
        ContaResponseDTO response = contaServices.criaConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário com email e senha")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
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

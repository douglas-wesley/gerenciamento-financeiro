package com.example.Gerenciamento_Financeiro.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de login")
public class LoginRequestDTO {
    @Email
    @Schema(description = "Email do usuário", example = "email@gmail.com")
    @NotNull
    private String email;

    @Schema(description = "Senha do usuário", example = "senha123")
    @NotNull
    private String password;
}

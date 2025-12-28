package com.example.Gerenciamento_Financeiro.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de login")
public class LoginRequestDTO {
    private String email;
    private String password;
}

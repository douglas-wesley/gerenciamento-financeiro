package com.example.Gerenciamento_Financeiro.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para respostas de login")
public class LoginResponseDTO {

    private String message;
    private String email;
    private boolean authenticated;
}

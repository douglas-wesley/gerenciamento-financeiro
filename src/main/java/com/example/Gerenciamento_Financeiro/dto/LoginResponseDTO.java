package com.example.Gerenciamento_Financeiro.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO para respostas de login")
public class LoginResponseDTO {

    @Schema(description = "Mensagem de resposta do login", example = "Login realizado com sucesso")
    private String message;
    @Email
    @Schema(description = "Email do usuário", example = "exemplo@gmail.com")
    private String email;
    @Schema(description = "Indica se o usuário foi autenticado com sucesso", example = "true")
    private boolean authenticated;
}

package com.example.Gerenciamento_Financeiro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO de requisição para criação ou atualização de conta bancária")
public class ContaRequestDTO {

    @Schema(description = "Nome do banco", example = "Banco do Brasil")
    @NotNull
    private String nomeBanco;
    @Schema(description = "Nome do titular da conta", example = "João Silva")
    private String nomeTitular;
    @Schema(description = "Número da agência bancária", example = "1234")
    @NotNull
    private Integer numeroConta;
    @Schema(description = "Email do titular da conta", example = "email@gmail.com")
    @Email
    @NotNull
    private String email;
    @Schema(description = "Senha da conta", example = "senha123")
    @NotNull
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
    @Schema(description = "Saldo inicial da conta", example = "2500.75")
    private BigDecimal saldo;
}

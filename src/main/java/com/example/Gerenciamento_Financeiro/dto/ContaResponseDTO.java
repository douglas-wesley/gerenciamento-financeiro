package com.example.Gerenciamento_Financeiro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de resposta para informações da conta bancária")
public class ContaResponseDTO {
    @Schema(description = "ID da conta bancária", example = "1")
    private Long id;
    @Schema(description = "Nome do banco", example = "Banco do Brasil")
    private String nomeBanco;
    @Schema(description = "Nome do titular da conta", example = "João Silva")
    private String nomeTitular;
    @Schema(description = "Número da agência bancária", example = "1234")
    private Integer numeroConta;
    @Schema(description = "Email do titular da conta", example = "email@gmail.com")
    private String email;
    @Schema(description = "Saldo atual da conta", example = "2500.75")
    private BigDecimal saldo;
}

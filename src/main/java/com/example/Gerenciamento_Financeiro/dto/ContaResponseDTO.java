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
    private Long id;
    private String nomeBanco;
    private String nomeTitular;
    private Integer numeroConta;
    private String email;
    private BigDecimal saldo;
}

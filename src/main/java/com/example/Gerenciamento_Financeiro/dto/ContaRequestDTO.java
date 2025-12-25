package com.example.Gerenciamento_Financeiro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO de requisição para criação ou atualização de conta bancária")
public class ContaRequestDTO {
    private String nomeBanco;
    private String nomeTitular;
    private Integer numeroConta;
    private BigDecimal saldo;
}

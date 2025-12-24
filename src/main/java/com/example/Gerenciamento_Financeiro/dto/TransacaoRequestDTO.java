package com.example.Gerenciamento_Financeiro.dto;

import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO para requisições de transações")
public class TransacaoRequestDTO {

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Tipo tipo;
}

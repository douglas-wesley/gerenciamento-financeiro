package com.example.Gerenciamento_Financeiro.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para respostas de transações")
public class TransacaoResponseDTO {

    @Schema(description = "ID da transação", example = "1")
    private Long id;
    @Schema(description = "Descrição da transação", example = "Compra no supermercado")
    private String descricao;
    @Schema(description = "Valor da transação", example = "150.75")
    private BigDecimal valor;
    @Schema(description = "Data da transação", example = "2024-06-15")
    private LocalDate data;
    @Schema(description = "Tipo da transação", example = "DESPESA")
    private String tipo;
    @Schema(description = "ID da conta associada à transação", example = "1")
    private Long contaId;
    @Schema(description = "IDs das categorias associadas à transação", example = "[10, 20]")
    private List<Long> categoriasIds;
}

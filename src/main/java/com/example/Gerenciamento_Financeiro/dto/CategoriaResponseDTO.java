package com.example.Gerenciamento_Financeiro.dto;

import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de resposta para informações da categoria")
public class CategoriaResponseDTO {
    @Schema(description = "ID da categoria", example = "1")
    private Long id;
    @Schema(description = "Nome da categoria", example = "Alimentação")
    private String nomeCategoria;
    @Schema(description = "Cor associada à categoria", example = "VERDE")
    private Cor cor;
}

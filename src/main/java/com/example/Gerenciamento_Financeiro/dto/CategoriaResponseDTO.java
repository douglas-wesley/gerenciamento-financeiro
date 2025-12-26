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
    private Long id;
    private String nomeCategoria;
    private Cor cor;
}

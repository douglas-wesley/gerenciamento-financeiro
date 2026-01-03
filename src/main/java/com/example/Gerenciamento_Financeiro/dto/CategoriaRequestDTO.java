package com.example.Gerenciamento_Financeiro.dto;


import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO de requisição para criação ou atualização de categoria")
public class CategoriaRequestDTO {
    @Schema(description = "Nome da categoria", example = "Alimentação")
    @NotNull
    private String nomeCategoria;
    @Schema(description = "Cor associada à categoria", example = "VERDE")
    @NotNull
    private Cor cor;
}

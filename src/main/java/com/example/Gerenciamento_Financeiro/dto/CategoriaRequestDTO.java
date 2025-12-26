package com.example.Gerenciamento_Financeiro.dto;


import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de requisição para criação ou atualização de categoria")
public class CategoriaRequestDTO {
    private String nomeCategoria;
    private Cor cor;
}

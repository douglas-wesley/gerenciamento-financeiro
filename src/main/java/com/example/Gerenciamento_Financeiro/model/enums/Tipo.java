package com.example.Gerenciamento_Financeiro.model.enums;

public enum Tipo {
    RECEITA,
    DESPESA;

    public static Tipo fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Valor do tipo não pode ser nulo ou vazio");
        }

        return switch (valor.toUpperCase().trim()) {
            case "RECEITA", "1" -> RECEITA;
            case "DESPESA", "2" -> DESPESA;
            default -> throw new IllegalArgumentException("Tipo inválido: " + valor + ". Valores aceitos: RECEITA, DESPESA, 1, 2");
        };
    }
}

package com.example.Gerenciamento_Financeiro.model;

import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_categoria")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties("transacoes")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCategoria;
    private Cor cor;

    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private List<Transacao> transacoes = new ArrayList<>();
}

package com.example.Gerenciamento_Financeiro.model;

import com.example.Gerenciamento_Financeiro.model.enums.Cor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_categoria")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCategoria;
    private Cor cor;

}

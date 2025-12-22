package com.example.Gerenciamento_Financeiro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_conta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta {

    @Id
    @GeneratedValue( strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String nomeBanco;
    private String nomeTitular;
    private String numeroConta;
    private Double saldo;
}

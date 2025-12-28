package com.example.Gerenciamento_Financeiro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private Integer numeroConta;

    @Column(unique = true)
    private String email;
    private String senha; // Fazer autenticação posteriormente
    private BigDecimal saldo;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY)
    private List<Transacao> transacoes = new ArrayList<>();
}

package com.example.Gerenciamento_Financeiro.model;

import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_transacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data; // Talvez instanceie isso na criação do objeto
    private Tipo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @ManyToMany
    @JoinTable(
        name = "transacao_categoria",
        joinColumns = @JoinColumn(name = "transacao_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;

    public void adicionarCategoria(Categoria categoria) {
        if (this.categorias == null) {
            this.categorias = new ArrayList<>();
        }
        this.categorias.add(categoria);
        categoria.getTransacoes().add(this);
    }

    public void removerCategoria(Categoria categoria) {
        if (this.categorias != null) {
            this.categorias.remove(categoria);
            categoria.getTransacoes().remove(this);
        }
    }
}

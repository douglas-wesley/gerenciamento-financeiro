package com.example.Gerenciamento_Financeiro.repository;


import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findAllByTipo(Tipo tipo);
    List<Transacao> findAllByConta(Conta conta);
}

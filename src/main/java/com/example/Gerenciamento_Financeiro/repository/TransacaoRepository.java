package com.example.Gerenciamento_Financeiro.repository;


import com.example.Gerenciamento_Financeiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}

package com.example.Gerenciamento_Financeiro.repository;

import com.example.Gerenciamento_Financeiro.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    boolean existsByNumeroConta(Integer numeroConta);
    boolean existsByEmail(String email);
}

package com.example.Gerenciamento_Financeiro.repository;

import com.example.Gerenciamento_Financeiro.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    boolean existsByNumeroConta(Integer numeroConta);
    boolean existsByEmail(String email);
    Optional<Conta> findByEmail(String email);
}

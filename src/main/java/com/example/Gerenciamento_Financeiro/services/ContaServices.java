package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContaServices {

    @Autowired
    private ContaRepository repository;

    // Ver isso na requisição, pois a exception não esta sendo ativa
    public ContaResponseDTO criaConta(ContaRequestDTO dto){ // ajustar as regras de negocio aqui também
        // Verificar se a conta já existe
        if (repository.existsByNumeroConta(dto.getNumeroConta())){
           throw new IllegalArgumentException("Número da conta já existe.");
        }

        // Verifica se ela esta com saldo negativo
        if (dto.getSaldo().compareTo(BigDecimal.ZERO) < 0.0){
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }

        // Adicionar mais regras depois

        Conta conta = new Conta();
        conta.setNomeBanco(dto.getNomeBanco());
        conta.setNomeTitular(dto.getNomeTitular());
        conta.setNumeroConta(dto.getNumeroConta());
        conta.setSaldo(dto.getSaldo());

        Conta novaConta = repository.save(conta);

        return new ContaResponseDTO(
                novaConta.getId(),
                novaConta.getNomeBanco(),
                novaConta.getNomeTitular(),
                novaConta.getNumeroConta(),
                novaConta.getSaldo()
        );

    }

    public ContaResponseDTO getContaById(Long id){
        Conta conta = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não existe.")); // Ajustar o status para 404
        return new ContaResponseDTO(
                conta.getId(),
                conta.getNomeBanco(),
                conta.getNomeTitular(),
                conta.getNumeroConta(),
                conta.getSaldo()
        );
    }

    public void deleteContaById(Long id){
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Conta não existe."); // Ajustar o status para 404
        } else {
            repository.deleteById(id);
        }
    }

    public ContaResponseDTO updateContaById(Long id, ContaRequestDTO dto){
        Conta contaExistente = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não existe.")); // Ajustar o status para 404

        // Verificar se o número da conta está sendo alterado para um que já existe
        if (!contaExistente.getNumeroConta().equals(dto.getNumeroConta()) &&
                repository.existsByNumeroConta(dto.getNumeroConta())) {
            throw new IllegalArgumentException("Número da conta já existe.");
        }

        // Atualizar os campos da conta existente
        contaExistente.setNomeBanco(dto.getNomeBanco());
        contaExistente.setNomeTitular(dto.getNomeTitular());
        contaExistente.setNumeroConta(dto.getNumeroConta());
        contaExistente.setSaldo(dto.getSaldo());

        Conta contaAtualizada = repository.save(contaExistente);

        return new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNomeBanco(),
                contaAtualizada.getNomeTitular(),
                contaAtualizada.getNumeroConta(),
                contaAtualizada.getSaldo()
        );
    }


}

package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaServices {

    @Autowired
    private ContaRepository repository;

    // Ver isso na requisição, pois a exception não esta sendo ativa
    public Conta criaConta(Conta conta){ // ajustar as regras de negocio aqui também
        // Verificar se a conta já existe
        if (repository.existsByNumeroConta(conta.getNumeroConta())){
           throw new IllegalArgumentException("Número da conta já existe.");
        }

        // Verifica se ela esta com saldo negativo
        if (conta.getSaldo() < 0.0){
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }

        return repository.save(conta);
    }

    public Conta getContaById(Long id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada BITCH."));
    }

    public void deleteContaById(Long id){
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Conta não existe."); // Ajustar o status para 404
        } else {
            repository.deleteById(id);
        }
    }


}

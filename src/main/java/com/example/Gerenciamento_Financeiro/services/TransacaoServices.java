package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransacaoServices {

    @Autowired
    private TransacaoRepository repository;

    public Transacao criaTransacao(Transacao transacao) { // ajustar as regras de negocio aqui
        return repository.save(transacao);
    }


}

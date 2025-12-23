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

    public void deletaTransacao(Long id) {
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Transação não existe."); // Ajustar o status para 404
        } else {
            repository.deleteById(id);
        }
    }

    public Transacao getTransacaoById(Long id){
        return repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Transação não encontrada."));
    }

    public Transacao updateTransacao(Long id, Transacao transacaoAtual){
        Transacao transacao = repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Transação não encontrada."));

        transacao.setData(transacaoAtual.getData());
        transacao.setDescricao(transacaoAtual.getDescricao());
        transacao.setTipo(transacaoAtual.getTipo());
        transacao.setValor(transacaoAtual.getValor());

        return repository.save(transacao);
    }
}

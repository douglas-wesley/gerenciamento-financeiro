package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import com.example.Gerenciamento_Financeiro.services.interfaces.ITransacaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransacaoServices implements ITransacaoServices {

    @Autowired
    private TransacaoRepository repository;

    @Override
    public TransacaoResponseDTO criaTransacao(TransacaoRequestDTO dto) { // ajustar as regras de negocio aqui
        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0.0){
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        };

        if (dto.getData() == null){
            throw new IllegalArgumentException("A data da transação não pode ser nula.");
        };

        if (dto.getTipo() == null || (dto.getTipo() != Tipo.RECEITA && dto.getTipo() != Tipo.DESPESA)){
            throw new IllegalArgumentException("Tipo de transação inválido.");
        };
        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());
        transacao.setTipo(dto.getTipo());

        Transacao novaTransacao = repository.save(transacao);

        return new TransacaoResponseDTO(
                novaTransacao.getId(),
                novaTransacao.getDescricao(),
                novaTransacao.getValor(),
                novaTransacao.getData(),
                novaTransacao.getTipo().toString()
        );
    }

    @Override
    public void deletaTransacao(Long id) {
        if (!repository.existsById(id)){
            throw new IllegalArgumentException("Transação não existe."); // Ajustar o status para 404
        } else if (repository.findById(id).get().getData().isBefore(LocalDate.now().minusMonths(6))) {
            throw new IllegalArgumentException("Transação com mais de 6 meses não pode ser deletada.");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public TransacaoResponseDTO getTransacaoById(Long id){ // Verificar
        Transacao transacao = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));
        return new TransacaoResponseDTO(transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo().toString());
    }

    // Adicionar um get para todos as transações

    // Ajustar isso
    @Override
    public TransacaoResponseDTO updateTransacao(Long id, TransacaoRequestDTO dto){
        Transacao transacao = repository.findById(id).orElseThrow( () -> new IllegalArgumentException("Transação não encontrada."));

        transacao.setData(dto.getData());
        transacao.setDescricao(dto.getDescricao());
        transacao.setTipo(dto.getTipo());
        transacao.setValor(dto.getValor());

        repository.save(transacao);

        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo().toString()
        );
    }
}

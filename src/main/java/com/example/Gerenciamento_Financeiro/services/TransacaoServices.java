package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Categoria;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.model.Transacao;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import com.example.Gerenciamento_Financeiro.repository.CategoriaRepository;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import com.example.Gerenciamento_Financeiro.repository.TransacaoRepository;
import com.example.Gerenciamento_Financeiro.services.interfaces.ITransacaoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransacaoServices implements ITransacaoServices {

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

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

        // Fazer validação de email

        // Senha deve ter pelo menos 6 dígitos

        Conta conta = contaRepository.findById(dto.getContaId()).orElseThrow(() -> new IllegalArgumentException("Conta não encontrada."));

        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());
        transacao.setTipo(dto.getTipo());
        transacao.setConta(conta);

        if (dto.getCategoriasIds() != null && !dto.getCategoriasIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriasIds());

            Set<Long> encontrados = categorias.stream()
                    .map(Categoria::getId)
                    .collect(Collectors.toSet());

            List<Long> faltantes = dto.getCategoriasIds().stream()
                    .filter(id -> !encontrados.contains(id))
                    .toList();

            if (!faltantes.isEmpty()) {
                throw new IllegalArgumentException("Categorias não encontradas: " + faltantes);
            }

            categorias.forEach(transacao::adicionarCategoria); // mantém relacionamento bidirecional
        }

        Transacao novaTransacao = repository.save(transacao);

        List<Long> categoriasIdsResp = (novaTransacao.getCategorias() != null)
                ? novaTransacao.getCategorias().stream().map(Categoria::getId).toList()
                : Collections.emptyList();


        return new TransacaoResponseDTO(
                novaTransacao.getId(),
                novaTransacao.getDescricao(),
                novaTransacao.getValor(),
                novaTransacao.getData(),
                novaTransacao.getTipo().toString(),
                conta.getId(),
                categoriasIdsResp
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
        List<Long> categoriaIds = (transacao.getCategorias() != null)
                ? transacao.getCategorias().stream().map(Categoria::getId).toList()
                : Collections.emptyList();
        return new TransacaoResponseDTO(transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo().toString(),
                transacao.getConta().getId(),
                categoriaIds);
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

        if (dto.getCategoriasIds() != null){
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriasIds());
            transacao.getCategorias().clear();
            categorias.forEach(transacao::adicionarCategoria);
        }

        repository.save(transacao);

        List<Long> categoriaIds = (transacao.getCategorias() != null)
                ? transacao.getCategorias().stream().map(Categoria::getId).toList()
                : Collections.emptyList();

        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo().toString(),
                transacao.getConta().getId(),
                categoriaIds
        );
    }
}

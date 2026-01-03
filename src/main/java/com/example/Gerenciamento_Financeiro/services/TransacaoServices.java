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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransacaoServices implements ITransacaoServices {


    private final TransacaoRepository repository;
    private final ContaRepository contaRepository;
    private final CategoriaRepository categoriaRepository;

    public TransacaoServices(TransacaoRepository repository, ContaRepository contaRepository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.contaRepository = contaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public TransacaoResponseDTO criaTransacao(TransacaoRequestDTO dto) { // ajustar as regras de negocio aqui
        if (dto == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os dados da transação não podem ser nulos.");
        };

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0.0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O valor da transação deve ser maior que zero.");
        };

        if (dto.getData() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"A data da transação não pode ser nula.");
        };

        if (dto.getTipo() == null || (dto.getTipo() != Tipo.RECEITA && dto.getTipo() != Tipo.DESPESA)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tipo de transação inválido.");
        };

        Conta conta = contaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Conta não encontrada."));

        BigDecimal saldoAtual = conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo();

        if (dto.getTipo() == Tipo.DESPESA){
            if (saldoAtual.compareTo(dto.getValor()) < 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para realizar a transação");
            }
            saldoAtual = saldoAtual.subtract(dto.getValor());
        } else {
            saldoAtual = saldoAtual.add(dto.getValor());
        }

        conta.setSaldo(saldoAtual);
        contaRepository.save(conta);

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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categorias não encontradas: " + faltantes);
            }

            categorias.forEach(transacao::adicionarCategoria); // mantém relacionamento bidirecional
        }

        Transacao novaTransacao = repository.save(transacao);

        return toResponseDTO(novaTransacao);
    }

    @Override
    @Transactional
    public void deletaTransacao(Long id) {
        Transacao transacao = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não existe."));

        if (transacao.getData().isBefore(LocalDate.now().minusMonths(6))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transação com mais de 6 meses não pode ser deletada.");
        }

        Conta conta = transacao.getConta();

        if (conta != null && transacao.getValor() != null) {
            if (transacao.getTipo() == Tipo.DESPESA) {
                conta.setSaldo((conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo()).add(transacao.getValor()));
            } else {
                conta.setSaldo((conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo()).subtract(transacao.getValor()));
            }
            contaRepository.save(conta);
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional
    public TransacaoResponseDTO getTransacaoById(Long id){ // Verificar
        Transacao transacao = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transação não encontrada."));
        return toResponseDTO(transacao);
    }

    // Adicionar um get para todos as transações

    @Override
    @Transactional
    public List<TransacaoResponseDTO> getAllTransacoes() {
        List<Transacao> transacoes = repository.findAll();

        if (transacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhuma transação encontrada.");
        }

        return transacoes.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<TransacaoResponseDTO> getAllTransacoesByConta(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        List<Transacao> transacoes = repository.findAllByConta(conta);

        if (transacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhuma transação encontrada para a conta " + contaId);
        }

        return transacoes.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<TransacaoResponseDTO> getAllTransacoesByTipo(Tipo tipo) {
        List<Transacao> transacoes = repository.findAllByTipo(tipo);

        if (transacoes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhuma transação do tipo " + tipo + " encontrada.");
        }

        return transacoes.stream().map(this::toResponseDTO).toList();
    }

    // Ajustar isso
    @Override
    @Transactional
    public TransacaoResponseDTO updateTransacao(Long id, TransacaoRequestDTO dto){
        if (dto == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Os dados da transação não podem ser nulos.");
        }

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor da transação deve ser maior que zero.");
        }

        if (dto.getTipo() == null || (dto.getTipo() != Tipo.RECEITA && dto.getTipo() != Tipo.DESPESA)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de transação inválido.");
        }

        Transacao transacao = repository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transação não encontrada."));

        Conta conta = transacao.getConta();

        if (conta == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta associada à transação não encontrada.");
        }

        // Reverter o impacto da transação antiga no saldo da conta
        BigDecimal oldValue = transacao.getValor();
        Tipo oldType = transacao.getTipo();

        if (oldType != null && oldValue != null) {
            if (oldType == Tipo.DESPESA) {
                conta.setSaldo((conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo()).add(oldValue));
            } else {
                conta.setSaldo((conta.getSaldo() == null ? BigDecimal.ZERO : conta.getSaldo()).subtract(oldValue));
            }
        }

        BigDecimal newValue = conta.getSaldo();

        if (dto.getTipo() == Tipo.DESPESA){
            if (conta.getSaldo() == null || conta.getSaldo().compareTo(dto.getValor()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para atualizar a transação.");
            }
            newValue = newValue.subtract(dto.getValor());
        } else {
            newValue = newValue.add(dto.getValor());
        }

        if (newValue.compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Saldo insuficiente para realizar a transação. Saldo atual: " +
                            conta.getSaldo() + ", valor da transação: " + dto.getValor());
        }

        conta.setSaldo(newValue);

        transacao.setData(dto.getData());
        transacao.setDescricao(dto.getDescricao());
        transacao.setTipo(dto.getTipo());
        transacao.setValor(dto.getValor());

        if (dto.getCategoriasIds() != null) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriasIds());
            Set<Long> encontrados = categorias.stream().map(Categoria::getId).collect(Collectors.toSet());
            List<Long> faltantes = dto.getCategoriasIds().stream()
                    .filter(i -> !encontrados.contains(i))
                    .toList();
            if (!faltantes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categorias não encontradas: " + faltantes);
            }
            transacao.getCategorias().clear();
            categorias.forEach(transacao::adicionarCategoria);
        }

        repository.save(transacao);
        contaRepository.save(conta);

        return toResponseDTO(transacao);
    }

    private TransacaoResponseDTO toResponseDTO(Transacao transacao) {
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

package com.example.Gerenciamento_Financeiro.services.interfaces;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;
import com.example.Gerenciamento_Financeiro.model.enums.Tipo;

import java.util.List;

public interface ITransacaoServices {
    TransacaoResponseDTO criaTransacao(TransacaoRequestDTO dto);
    void deletaTransacao(Long id);
    TransacaoResponseDTO getTransacaoById(Long id);
    TransacaoResponseDTO updateTransacao(Long id, TransacaoRequestDTO dto);
    List<TransacaoResponseDTO> getAllTransacoesByTipo(Tipo tipo);
    List<TransacaoResponseDTO> getAllTransacoes();
    List<TransacaoResponseDTO> getAllTransacoesByConta(Long contaId);
}

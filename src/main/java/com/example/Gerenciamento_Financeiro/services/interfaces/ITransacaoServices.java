package com.example.Gerenciamento_Financeiro.services.interfaces;

import com.example.Gerenciamento_Financeiro.dto.TransacaoRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.TransacaoResponseDTO;

public interface ITransacaoServices {
    TransacaoResponseDTO criaTransacao(TransacaoRequestDTO dto);
    void deletaTransacao(Long id);
    TransacaoResponseDTO getTransacaoById(Long id);
    TransacaoResponseDTO updateTransacao(Long id, TransacaoRequestDTO dto);
}

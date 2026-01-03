package com.example.Gerenciamento_Financeiro.services.interfaces;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;

import java.util.List;

public interface IContaServices {
    ContaResponseDTO criaConta(ContaRequestDTO dto);
    ContaResponseDTO getContaById(Long id);
    void deleteContaById(Long id);
    ContaResponseDTO updateContaById(Long id, ContaRequestDTO dto);
    List<ContaResponseDTO> getAllContas();

}

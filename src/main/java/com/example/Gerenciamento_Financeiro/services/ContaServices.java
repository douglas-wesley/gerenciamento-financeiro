package com.example.Gerenciamento_Financeiro.services;

import com.example.Gerenciamento_Financeiro.dto.ContaRequestDTO;
import com.example.Gerenciamento_Financeiro.dto.ContaResponseDTO;
import com.example.Gerenciamento_Financeiro.model.Conta;
import com.example.Gerenciamento_Financeiro.repository.ContaRepository;
import com.example.Gerenciamento_Financeiro.services.interfaces.IContaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class ContaServices implements IContaServices {


    private final ContaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ContaServices(ContaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ContaResponseDTO criaConta(ContaRequestDTO dto){ // ajustar as regras de negocio aqui também
        // Verificar se a conta já existe
        if (dto == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da conta não podem ser nulos.");
        }

        if (repository.existsByNumeroConta(dto.getNumeroConta())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número da conta já existe.");
        }

        if (dto.getSaldo() == null || dto.getSaldo().compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo inicial não pode ser negativo.");
        }

        if (dto.getNomeBanco() == null || dto.getNomeBanco().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do banco não pode ser vazio.");
        }

        if (dto.getEmail() == null || dto.getEmail().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório.");
        }

        if (repository.existsByEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma conta com esse email.");
        }

        if (dto.getSenha() == null || dto.getSenha().length() < 6){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
        }


        if (!isValidEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido.");
        }


        Conta conta = new Conta();
        conta.setNomeBanco(dto.getNomeBanco());
        conta.setNomeTitular(dto.getNomeTitular());
        conta.setNumeroConta(dto.getNumeroConta());
        conta.setSaldo(dto.getSaldo());
        conta.setEmail(dto.getEmail());
        conta.setSenha(passwordEncoder.encode(dto.getSenha()));

        Conta novaConta = repository.save(conta);

        return new ContaResponseDTO(
                novaConta.getId(),
                novaConta.getNomeBanco(),
                novaConta.getNomeTitular(),
                novaConta.getNumeroConta(),
                novaConta.getEmail(),
                novaConta.getSaldo()
        );

    }


    @Override
    public ContaResponseDTO getContaById(Long id){
        Conta conta = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Conta não existe.")); // Ajustar o status para 404
        return new ContaResponseDTO(
                conta.getId(),
                conta.getNomeBanco(),
                conta.getNomeTitular(),
                conta.getNumeroConta(),
                conta.getEmail(),
                conta.getSaldo()
        );
    }


    @Override
    public void deleteContaById(Long id){
        if (!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Conta não existe."); // Ajustar o status para 404
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public ContaResponseDTO updateContaById(Long id, ContaRequestDTO dto){
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados para atualização são obrigatórios.");
        }

        Conta contaExistente = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Conta não existe.")); // Ajustar o status para 404

        if (!Objects.equals(contaExistente.getNumeroConta(), dto.getNumeroConta()) &&
                repository.existsByNumeroConta(dto.getNumeroConta())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número da conta já existe.");
        }

        // Verificar se o email está sendo alterado para um que já existe
        if (!Objects.equals(contaExistente.getEmail(), dto.getEmail()) &&
                repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já em uso por outra conta.");
        }


        if (dto.getSaldo() == null || dto.getSaldo().compareTo(BigDecimal.ZERO) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo não pode ser negativo.");
        }

        // Atualizar os campos da conta existente
        contaExistente.setNomeBanco(dto.getNomeBanco());
        contaExistente.setNomeTitular(dto.getNomeTitular());
        contaExistente.setNumeroConta(dto.getNumeroConta());
        contaExistente.setSaldo(dto.getSaldo());
        contaExistente.setEmail(dto.getEmail());

        if (dto.getSenha() != null || !dto.getSenha().isEmpty()){
            if (dto.getSenha().length() < 6){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
            }
            contaExistente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (dto.getEmail() == null || dto.getEmail().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório.");
        }

        if (!isValidEmail(dto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido.");
        }

        if (dto.getSenha() == null || dto.getSenha().length() < 6){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
        }


        Conta contaAtualizada = repository.save(contaExistente);

        return new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNomeBanco(),
                contaAtualizada.getNomeTitular(),
                contaAtualizada.getNumeroConta(),
                contaAtualizada.getEmail(),
                contaAtualizada.getSaldo()
        );
    }

    private boolean isValidEmail(String email) {
        // Implementar uma validação de email mais robusta se necessário
        if (email == null || email.isEmpty()) return false;

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@') || atIndex == email.length() - 1) {
            return false; // sem '@', múltiplos '@' ou parte local/domínio vazios
        }

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        // Validações para parte local
        if (local.startsWith(".") || local.endsWith(".") || local.contains("..")) return false;
        if (!local.matches("^[A-Za-z0-9!#$%&'*+/=?^_`{|}~.-]+$")) return false;

        // Validações para domínio
        if (domain.startsWith(".") || domain.endsWith(".") || domain.contains("..") || !domain.contains(".")) return false;
        String[] labels = domain.split("\\.");
        for (String label : labels) {
            if (label.isEmpty()) return false;
            if (!label.matches("^[A-Za-z0-9-]+$")) return false;
            if (label.startsWith("-") || label.endsWith("-")) return false;
        }

        // TLD deve ter pelo menos 2 caracteres e ser apenas letras
        String tld = labels[labels.length - 1];
        if (tld.length() < 2 || !tld.matches("^[A-Za-z]+$")) return false;

        return true;
    }


}

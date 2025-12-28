package com.example.Gerenciamento_Financeiro.security;

import com.example.Gerenciamento_Financeiro.model.Conta;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ContaUserDetails implements UserDetails {
    private final Conta conta;

    public ContaUserDetails(Conta conta) {
        this.conta = conta;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Sem roles por enquanto: lista vazia
        return List.of();
    }

    @Override
    public String getPassword() {
        return conta.getSenha();
    }

    @Override
    public String getUsername() {
        return conta.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Conta getConta() {
        return conta;
    }
}

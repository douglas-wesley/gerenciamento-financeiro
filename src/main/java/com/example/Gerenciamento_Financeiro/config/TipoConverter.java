package com.example.Gerenciamento_Financeiro.config;

import com.example.Gerenciamento_Financeiro.model.enums.Tipo;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class TipoConverter implements Converter<String, Tipo>{

    @Override
    public Tipo convert(String source){
        return Tipo.fromString(source);
    }
}

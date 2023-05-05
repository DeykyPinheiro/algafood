package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public record CozinhaDTO( Long id, String nome) {
    public CozinhaDTO(Cozinha data) {
        this(data.getId(), data.getNome());
    }
}


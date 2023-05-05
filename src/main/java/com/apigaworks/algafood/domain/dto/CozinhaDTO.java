package com.apigaworks.algafood.domain.dto;

import com.apigaworks.algafood.domain.model.Cozinha;

import java.math.BigDecimal;

public record CozinhaDTO(Long id, String nome) {

    public CozinhaDTO(Cozinha data) {
        this(data.getId(), data.getNome());
    }
}

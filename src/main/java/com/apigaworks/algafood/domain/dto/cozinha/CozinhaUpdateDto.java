package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;

public record CozinhaUpdateDto(Long id) {
    public CozinhaUpdateDto(Cozinha cozinha) {
        this(cozinha.getId());
    }
}


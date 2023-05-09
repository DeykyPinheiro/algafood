package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;

public record CozinhaDto(Long id, String nome) {
    public CozinhaDto (Cozinha cozinha){
        this(cozinha.getId(), cozinha.getNome());
    }
}

package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;
import io.swagger.v3.oas.annotations.media.Schema;

public record CozinhaDto(
    
        @Schema(example = "1")
        Long id,

        @Schema(example = "Mineira")
        String nome) {

    public CozinhaDto(Cozinha cozinha) {
        this(cozinha.getId(), cozinha.getNome());
    }
}

package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;
import io.swagger.v3.oas.annotations.media.Schema;

public record CozinhaUpdateDto(

        @Schema(example = "1")
        Long id) {
    public CozinhaUpdateDto(Cozinha cozinha) {
        this(cozinha.getId());
    }
}


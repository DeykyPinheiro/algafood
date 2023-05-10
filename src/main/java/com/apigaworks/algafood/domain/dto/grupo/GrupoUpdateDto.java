package com.apigaworks.algafood.domain.dto.grupo;

import jakarta.validation.constraints.NotBlank;

public record GrupoUpdateDto(

        @NotBlank
        String nome
) {
}

package com.apigaworks.algafood.domain.dto.grupo;

import jakarta.validation.constraints.NotBlank;

public record GrupoSaveDto(

        @NotBlank
        String nome
) {
}

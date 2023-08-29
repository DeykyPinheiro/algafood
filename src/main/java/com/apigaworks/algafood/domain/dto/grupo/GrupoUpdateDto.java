package com.apigaworks.algafood.domain.dto.grupo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GrupoUpdateDto(

        @Schema(example = "Gerente")
        @NotBlank
        String nome
) {
}

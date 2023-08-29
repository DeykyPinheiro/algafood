package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoDto(


        @Schema(example = "1")
        Long id,

        @Schema(example = "Gerente")
        String nome

) {
    public GrupoDto(Grupo grupo) {
        this(grupo.getId(), grupo.getNome());
    }
}

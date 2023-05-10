package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoDto(

        Long id,
        String nome

) {
    public GrupoDto(Grupo grupo) {
        this(grupo.getId(), grupo.getNome());
    }
}

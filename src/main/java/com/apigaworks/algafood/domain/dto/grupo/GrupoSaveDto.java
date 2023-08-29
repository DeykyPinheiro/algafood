package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record GrupoSaveDto(

        @Schema(example = "Gerente")
        @NotBlank
        String nome
) {

        public GrupoSaveDto (Grupo grupo){
                this(grupo.getNome());

        }
}

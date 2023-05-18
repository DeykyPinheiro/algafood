package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;
import jakarta.validation.constraints.NotBlank;

public record GrupoSaveDto(

        @NotBlank
        String nome
) {

        public GrupoSaveDto (Grupo grupo){
                this(grupo.getNome());

        }
}

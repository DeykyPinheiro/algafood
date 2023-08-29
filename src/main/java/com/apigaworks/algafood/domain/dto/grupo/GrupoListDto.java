package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record GrupoListDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Gerente")
        String nome){

    public GrupoListDto(Grupo grupo) {
        this(grupo.getId(), grupo.getNome());
    }

    public static List<GrupoListDto> converterLista(Collection<Grupo> listaGrupos){
        return listaGrupos.stream().map(GrupoListDto::new).collect(Collectors.toList());
    }

}

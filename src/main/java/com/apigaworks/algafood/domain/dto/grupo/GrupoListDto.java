package com.apigaworks.algafood.domain.dto.grupo;

import com.apigaworks.algafood.domain.model.Grupo;

import java.util.List;
import java.util.stream.Collectors;

public record GrupoListDto(Long id, String nome){

    public GrupoListDto(Grupo grupo) {
        this(grupo.getId(), grupo.getNome());
    }

    public static List<GrupoListDto> converterLista(List<Grupo> listaGrupos){
        return listaGrupos.stream().map(GrupoListDto::new).collect(Collectors.toList());
    }

}

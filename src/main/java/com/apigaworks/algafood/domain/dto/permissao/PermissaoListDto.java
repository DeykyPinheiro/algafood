package com.apigaworks.algafood.domain.dto.permissao;

import com.apigaworks.algafood.domain.model.Permissao;

import java.util.List;
import java.util.stream.Collectors;

public record PermissaoListDto(

        Long id,

        String nome,

        String descricao

) {

    public PermissaoListDto(Permissao permissao) {
        this(permissao.getId(), permissao.getNome(), permissao.getDescricao());
    }

    public static List<PermissaoListDto> converterLista(List<Permissao> listaPermissao) {
        return listaPermissao.stream().map(PermissaoListDto::new).collect(Collectors.toList());
    }
}

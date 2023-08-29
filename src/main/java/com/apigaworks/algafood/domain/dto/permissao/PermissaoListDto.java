package com.apigaworks.algafood.domain.dto.permissao;

import com.apigaworks.algafood.domain.model.Permissao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record PermissaoListDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Gerente")
        String nome,

        @Schema(example = "Gerencia a loja")
        String descricao

) {

    public PermissaoListDto(Permissao permissao) {
        this(permissao.getId(), permissao.getNome(), permissao.getDescricao());
    }

    public static List<PermissaoListDto> converterLista(Collection<Permissao> listaPermissao) {
        return listaPermissao.stream().map(PermissaoListDto::new).collect(Collectors.toList());
    }
}

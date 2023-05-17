package com.apigaworks.algafood.domain.dto.permissao;

import com.apigaworks.algafood.domain.model.Permissao;

public record PermissaoDto(

        Long id,

        String nome,

        String descricao
) {
    public PermissaoDto(Permissao permissao) {
        this(permissao.getId(), permissao.getNome(), permissao.getDescricao());
    }
}

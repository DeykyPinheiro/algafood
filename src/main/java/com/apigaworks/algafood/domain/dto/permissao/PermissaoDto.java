package com.apigaworks.algafood.domain.dto.permissao;

import com.apigaworks.algafood.domain.model.Permissao;
import io.swagger.v3.oas.annotations.media.Schema;

public record PermissaoDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Gerente")
        String nome,

        @Schema(example = "Gerencia loja")
        String descricao
) {
    public PermissaoDto(Permissao permissao) {
        this(permissao.getId(), permissao.getNome(), permissao.getDescricao());
    }
}

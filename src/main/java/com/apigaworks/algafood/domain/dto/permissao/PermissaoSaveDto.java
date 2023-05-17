package com.apigaworks.algafood.domain.dto.permissao;

import jakarta.validation.constraints.NotBlank;

public record PermissaoSaveDto(


        @NotBlank
        String nome,

        @NotBlank
        String descricao
) {
}

package com.apigaworks.algafood.domain.dto.permissao;

import jakarta.validation.constraints.NotBlank;

public record PermissaoUpdateDto(
        @NotBlank
        String nome,

        @NotBlank
        String descricao
) {
}

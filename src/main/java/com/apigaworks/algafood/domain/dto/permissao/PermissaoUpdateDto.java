package com.apigaworks.algafood.domain.dto.permissao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PermissaoUpdateDto(
        @NotBlank
        @Schema(example = "Gerente")
        String nome,

        @NotBlank
        @Schema(example = "Gerencia a loja")
        String descricao
) {
}

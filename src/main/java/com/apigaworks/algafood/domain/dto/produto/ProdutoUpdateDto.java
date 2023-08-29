package com.apigaworks.algafood.domain.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoUpdateDto(

        @NotBlank
        @Schema(example = "bife ancho")
        String nome,

        @NotBlank
        @Schema(example = "corte macio")
        String descricao,

        @NotNull
        @Schema(example = "20.00")
        BigDecimal preco,

        @NotNull
        @Schema(example = "true")
        Boolean ativo
) {
}

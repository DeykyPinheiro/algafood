package com.apigaworks.algafood.domain.dto.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoUpdateDto(

        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotNull
        BigDecimal preco,

        @NotNull
        Boolean ativo
) {
}

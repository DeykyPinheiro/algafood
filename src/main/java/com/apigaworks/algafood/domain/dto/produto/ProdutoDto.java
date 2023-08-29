package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoDto(

        @Schema(example = "1")
        Long id,

        @NotBlank
        @Schema(example = "Bife Ancho")
        String nome,

        @NotBlank@Schema(example = "1")
        String descricao,

        @NotNull
        @Schema(example = "50.00")
        BigDecimal preco,

        @NotNull
        @Schema(example = "true")
        Boolean ativo

) {

    public ProdutoDto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getAtivo());
    }
}

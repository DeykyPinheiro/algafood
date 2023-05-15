package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoDto(

        Long id,

        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotNull
        BigDecimal preco,

        @NotNull
        Boolean ativo

) {

    public ProdutoDto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getAtivo());
    }
}

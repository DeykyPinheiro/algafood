package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ProdutoListDto(
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
    public ProdutoListDto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getAtivo());
    }

    public static List<ProdutoListDto> converterLista(Set<Produto> listaProdutos) {
        return listaProdutos.stream().map(ProdutoListDto::new).collect(Collectors.toList());
    }
}

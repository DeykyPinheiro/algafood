package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ProdutoListDto(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Bifé")
        @NotBlank
        String nome,

        @NotBlank
        @Schema(example = "bife ancho")
        String descricao,

        @Schema(example = "20.00")
        @NotNull
        BigDecimal preco,

        @Schema(example = "bife")
        @NotNull
        Boolean ativo
) {
    public ProdutoListDto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getAtivo());
    }

    public static List<ProdutoListDto> converterLista(Collection<Produto> listaProdutos) {
        return listaProdutos.stream().map(ProdutoListDto::new).collect(Collectors.toList());
    }
}

package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoSaveDto(

        @NotBlank
        @Schema(example = "bife Ancho")
        String nome,

        @Schema(example = "corte macio e suculento")
        @NotBlank
        String descricao,

        @NotNull
        @Schema(example = "20.00")
        BigDecimal preco,

        @NotNull
        @Schema(example = "true")
        Boolean ativo

) {
        public ProdutoSaveDto(Produto produto) {
                this(produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getAtivo());
        }
}
//{
//        "nome": "produto1",
//        "descricao": "isso é o produto1",
//        "preco": "10,50",
//        "ativo": true
//        }
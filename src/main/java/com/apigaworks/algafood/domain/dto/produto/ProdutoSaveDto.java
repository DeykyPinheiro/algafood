package com.apigaworks.algafood.domain.dto.produto;

import com.apigaworks.algafood.domain.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoSaveDto(

        @NotBlank
        String nome,

        @NotBlank
        String descricao,

        @NotNull
        BigDecimal preco,

        @NotNull
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
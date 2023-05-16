package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;

import java.util.Optional;

public interface CustomProdutoRespository {
    Optional<Produto> buscarProdutoPorIdPorRestaurante(Long idRestaurante, Long idProduto);
}

package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;

import java.util.Optional;

//interface criada para implementar consultas customizadas do produtoRepository
public interface CustomProdutoRespository {

    Optional<Produto> buscarProdutoPorIdPorRestaurante(Long idRestaurante, Long idProduto);
}

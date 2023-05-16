package com.apigaworks.algafood.infrastructure.repository;

import com.apigaworks.algafood.domain.exception.ProdutoNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.repository.CustomProdutoRespository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProdutoRespositoryImpl implements CustomProdutoRespository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Produto> buscarProdutoPorIdPorRestaurante(Long idRestaurante, Long idProduto) {
//        System.out.println("dentro buscarProdutoPorId: " + idRestaurante);
//        System.out.println("dentro buscarProducePorId: " + idProduto);

        var query = new StringBuilder();
        query.append("select p from Produto as p");
        query.append(" where p.restaurante.id = :idRestaurante");
        query.append(" and p.id = :idProduto");


        String queryText = query.toString();
        System.out.println("sou query selecionada " + queryText);


        try {
            Produto produto = manager.createQuery(queryText, Produto.class)
                    .setParameter("idRestaurante", idRestaurante)
                    .setParameter("idProduto", idProduto)
                    .getSingleResult();
            return Optional.ofNullable(produto);
        } catch (Exception ex) {
            throw new ProdutoNaoEncontratoException(idRestaurante, idProduto);
        }
    }
}

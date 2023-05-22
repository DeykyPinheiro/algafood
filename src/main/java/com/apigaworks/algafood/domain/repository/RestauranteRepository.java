package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    @Query("from Produto where restaurante.id = :restauranteId and id = :produtoId")
    Optional<Produto> encontrarProduto(Long restauranteId, Long produtoId);
}

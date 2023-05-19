package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
    List<Pedido> findAll();
}

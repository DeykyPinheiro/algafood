package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

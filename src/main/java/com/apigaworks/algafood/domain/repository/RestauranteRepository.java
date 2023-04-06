package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}

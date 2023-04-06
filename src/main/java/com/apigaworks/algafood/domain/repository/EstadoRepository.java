package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

}

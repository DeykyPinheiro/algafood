package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRespository extends JpaRepository<Produto, Long> {
}

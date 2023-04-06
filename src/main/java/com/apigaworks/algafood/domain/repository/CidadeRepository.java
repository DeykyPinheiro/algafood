package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {


}

package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {


}
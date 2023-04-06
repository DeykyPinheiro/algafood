package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {



}

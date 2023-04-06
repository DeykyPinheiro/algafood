package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

}

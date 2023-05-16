package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//extendendo duas interfaces uma o JPA padrao e outra com minhas
// queries customizadas
public interface ProdutoRespository extends JpaRepository<Produto, Long>, CustomProdutoRespository {

}

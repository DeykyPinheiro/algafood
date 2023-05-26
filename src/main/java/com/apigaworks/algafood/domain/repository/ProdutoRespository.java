package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.model.Restaurante;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//extendendo duas interfaces uma o JPA padrao e outra com minhas
// queries customizadas
public interface ProdutoRespository extends JpaRepository<Produto, Long>, CustomProdutoRespository {

    @Query("from Produto where restaurante.id = :restauranteId and id = :produtoId")
    Optional<Produto> encontrarProduto(Long restauranteId, Long produtoId);

    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findAtivosByRestaurante(Restaurante restaurante);

}

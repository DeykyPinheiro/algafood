package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {


//    consulta se o usuario é responsavel pelo restaurante
    @Query("SELECT CASE WHEN COUNT(rest) > 0 THEN true ELSE false END " +
            "FROM Restaurante rest " +
            "JOIN rest.listaUsuario resp " +
            "WHERE rest.id = :restauranteId AND resp.id = :usuarioId")
    boolean existsResponvavel(Long restauranteId, Long usuarioId);


}

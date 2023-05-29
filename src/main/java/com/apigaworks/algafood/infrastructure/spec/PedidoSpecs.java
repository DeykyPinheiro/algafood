package com.apigaworks.algafood.infrastructure.spec;

import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.filter.PedidoFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;


public class PedidoSpecs {

    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
        return ((root, query, builder) -> {

            root.fetch("restaurante").fetch("cozinha");
            root.fetch("cliente");

            List<Predicate> predicates = new ArrayList<Predicate>();

            if (filtro.clienteId() != null){
                predicates.add(builder.equal(root.get("cliente"), filtro.clienteId()));
            }

//            primeiro é o nome do campo da entidade que vc ta comparadno(vem do root), no caso pedido, e com o que depois
            if (filtro.restauranteId() != null){
                predicates.add(builder.equal(root.get("restaurante"), filtro.restauranteId()));
            }

            if (filtro.dataCriacaoInicio() != null){
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.dataCriacaoInicio()));
            }

            if (filtro.dataCriacaofim() != null){
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.dataCriacaofim()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
}

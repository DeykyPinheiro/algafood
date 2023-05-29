package com.apigaworks.algafood.infrastructure.service;

import com.apigaworks.algafood.domain.enumerated.StatusPedido;
import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;
import com.apigaworks.algafood.domain.service.VendaQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {


    @PersistenceContext
    private EntityManager manager;


    @Override
    public List<VendaDiaria> consultarVendaDiarias(VendaDiariaFilter filtro) {
//        crio um builder do criteria
        var builder = manager.getCriteriaBuilder();
//        crio uma query quer vai retonar entidades de venda diaria
        var query = builder.createQuery(VendaDiaria.class);
//        aqui é o "from" da onde essa query vai vir
        var root = query.from(Pedido.class);

        List<Predicate> predicates = new ArrayList<Predicate>();

//        estou acessando a funcao do banco sql, 1º funcao, 2º o que deve retornar,
//        3º qual o campo usar
        var functionDateDataCriacao = builder.function("date", LocalDate.class,
                root.get("dataCriacao"));

//        aqui é o select primeiro eu digo o que vai retornar,
//        depois comeco a usar os campos em ordem do construtor,
//        data, id, e valor total
        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));


//        implemento os filtros semelhante ao pedidofilter
        if (filtro.restauranteId() != null) {
            predicates.add(builder.equal(root.get("restaurante"), filtro.restauranteId()));
        }

        if (filtro.dataCriacaoInicio() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.dataCriacaoInicio()));
        }

        if (filtro.dataCriacaofim() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.dataCriacaofim()));
        }


//        filtra apenas de tiver esses statos no campo
        predicates.add(root.get("statusPedido").in(
                StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));


//        faco efetivamente a selecao
        query.select(selection);

//        onde sao aplicados os filtro
        query.where(predicates.toArray(new Predicate[0]));

//        agrupo por data
        query.groupBy(functionDateDataCriacao);

//        retorno o resultado
        return manager.createQuery(query).getResultList();
    }
}

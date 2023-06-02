package com.apigaworks.algafood.infrastructure.repository;

import com.apigaworks.algafood.domain.model.FotoProduto;
import com.apigaworks.algafood.domain.repository.ProdutoRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepositoryQueriesImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

//    isso aqui serve pra salvar com o mesmo repository de produto ja que dentro da mesma
//    raiz só podemos ter um repository
    @Override
    @Transactional
    public FotoProduto save(FotoProduto foto) {
        return manager.merge(foto);
    }
}

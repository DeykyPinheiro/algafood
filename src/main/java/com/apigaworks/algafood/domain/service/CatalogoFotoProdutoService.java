package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.FotoProduto;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogoFotoProdutoService {


    private ProdutoRespository produtoRespository;

    @Autowired
    public CatalogoFotoProdutoService(ProdutoRespository produtoRespository) {
        this.produtoRespository = produtoRespository;
    }

//    pra esse save funcionar eu tive que implementar um novo seric edentro do mesmo repository,
//    produto no caso
    @Transactional
    public FotoProduto salvar(FotoProduto foto) {
        return produtoRespository.save(foto);
    }


}

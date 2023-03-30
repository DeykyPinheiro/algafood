package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Cozinha;

import java.util.List;

public interface CozinhaRepository {

    List<Cozinha> listar();

    Cozinha buscar(Long id);

    Cozinha salvar(Cozinha cozinha);

    void remover(Cozinha cozinha);
}

package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Estado;

import java.util.List;

public interface EstadoRepository {
    List<Estado> listar();

    Estado buscar(Long id);

    Estado salvar(Estado estado);

    void remover(Estado estado);

}

package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.model.Permissao;

import java.util.List;

public interface PermissaoRepository {

    List<Permissao> listar();

    Permissao buscar(Long id);

    Permissao salvar(Permissao permissao);

    void remover(Permissao permissao);


}

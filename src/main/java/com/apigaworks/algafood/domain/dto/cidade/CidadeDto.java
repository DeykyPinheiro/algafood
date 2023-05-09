package com.apigaworks.algafood.domain.dto.cidade;

import com.apigaworks.algafood.domain.model.Cidade;

public record CidadeDto(Long id, String nome){

    public CidadeDto(Cidade cidade) {
        this(cidade.getId(), cidade.getNome());
    }
}

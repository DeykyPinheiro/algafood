package com.apigaworks.algafood.domain.dto.cidade;

import com.apigaworks.algafood.domain.model.Cidade;
import org.modelmapper.ModelMapper;

public record CidadeUpdateDto(Long id) {



    public CidadeUpdateDto(Cidade cidade) {
        this(cidade.getId());
    }
}

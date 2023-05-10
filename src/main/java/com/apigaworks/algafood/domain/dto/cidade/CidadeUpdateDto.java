package com.apigaworks.algafood.domain.dto.cidade;

import com.apigaworks.algafood.domain.model.Cidade;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;

public record CidadeUpdateDto(

        @NotNull Long id
) {


    public CidadeUpdateDto(Cidade cidade) {
        this(cidade.getId());
    }
}

package com.apigaworks.algafood.domain.dto.cidade;

import com.apigaworks.algafood.domain.model.Cidade;
import io.swagger.v3.oas.annotations.media.Schema;

public record CidadeDto(

        @Schema(example = "1")
        Long id,
        
        @Schema(example = "Sao Paulo")
        String nome){

    public CidadeDto(Cidade cidade) {
        this(cidade.getId(), cidade.getNome());
    }
}

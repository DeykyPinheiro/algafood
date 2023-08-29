package com.apigaworks.algafood.domain.dto.cidade;

import com.apigaworks.algafood.domain.model.Cidade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;

public record CidadeUpdateDto(

        @Schema(example = "1")
        @NotNull Long id
) {


    public CidadeUpdateDto(Cidade cidade) {
        this(cidade.getId());
    }
}

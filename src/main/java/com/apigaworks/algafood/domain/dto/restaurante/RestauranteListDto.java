package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDto;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.model.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record RestauranteListDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "tuk tuk")
        String nome,

        @Schema(example = "1.00")
        BigDecimal taxaFrete,

        CozinhaDto cozinha,

        @Schema(example = "true")
        Boolean ativo,

        @Schema(example = "true")
        Boolean aberto



) {

    public RestauranteListDto(Restaurante restaurante) {
        this(restaurante.getId(), restaurante.getNome(),restaurante.getTaxaFrete(),
                new CozinhaDto(restaurante.getCozinha()), restaurante.getAtivo(), restaurante.getAberto());
    }
    public static List<RestauranteListDto> converterLista(Collection<Restaurante> lista) {
        return lista.stream().map(RestauranteListDto::new).collect(Collectors.toList());
    }
}

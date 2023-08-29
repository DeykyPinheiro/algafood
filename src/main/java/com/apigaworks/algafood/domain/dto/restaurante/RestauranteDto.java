package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDto;
import com.apigaworks.algafood.domain.dto.cozinha.CozinhaUpdateDto;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.model.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record RestauranteDto(

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
        Boolean aberto,

        EnderecoDto endereco) {


    public RestauranteDto(Restaurante restaurante) {

        this(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getTaxaFrete(),
                restaurante.getCozinha() != null ? new CozinhaDto(restaurante.getCozinha()): null,
                restaurante.getAtivo(),
                restaurante.getAberto(),
                restaurante.getEndereco() != null ? new EnderecoDto(restaurante.getEndereco()) : null

        );


    }

}



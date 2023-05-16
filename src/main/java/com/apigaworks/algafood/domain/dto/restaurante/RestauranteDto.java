package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDto;
import com.apigaworks.algafood.domain.dto.cozinha.CozinhaUpdateDto;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.model.Restaurante;

import java.math.BigDecimal;

public record RestauranteDto(

        Long id,

        String nome,

        BigDecimal taxaFrete,

        CozinhaDto cozinha,

        Boolean ativo,
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



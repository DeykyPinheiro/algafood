package com.apigaworks.algafood.domain.dto;

import com.apigaworks.algafood.domain.model.Restaurante;

import java.math.BigDecimal;

public record RestauranteDTO(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha) {

    public RestauranteDTO(Restaurante data) {
        this(data.getId(), data.getNome(),data.getTaxaFrete(), new CozinhaDTO( data.getCozinha()));
    }
}

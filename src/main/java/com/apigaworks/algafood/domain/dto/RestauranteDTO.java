package com.apigaworks.algafood.domain.dto;

import java.math.BigDecimal;

public record RestauranteDTO(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha) {

}

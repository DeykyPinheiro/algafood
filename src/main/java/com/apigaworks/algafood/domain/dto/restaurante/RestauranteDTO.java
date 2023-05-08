package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDTO;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public record RestauranteDTO(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha, Boolean ativo) {


    public RestauranteDTO(Restaurante data) {
        this(data.getId(), data.getNome(), data.getTaxaFrete(), new CozinhaDTO(data.getCozinha()), data.getAtivo());
    }
}

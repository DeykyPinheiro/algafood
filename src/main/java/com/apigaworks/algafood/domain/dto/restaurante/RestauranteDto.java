package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDTO;
import com.apigaworks.algafood.domain.dto.endereco.EnderecoDto;
import com.apigaworks.algafood.domain.model.Endereco;
import com.apigaworks.algafood.domain.model.Restaurante;

import java.math.BigDecimal;

public record RestauranteDto(Long id, String nome, BigDecimal taxaFrete, CozinhaDTO cozinha, Boolean ativo, EnderecoDto endereco) {


    public RestauranteDto(Restaurante data) {
        this(data.getId(), data.getNome(), data.getTaxaFrete(), new CozinhaDTO(data.getCozinha()), data.getAtivo(), new EnderecoDto(data.getEndereco()));
    }
}

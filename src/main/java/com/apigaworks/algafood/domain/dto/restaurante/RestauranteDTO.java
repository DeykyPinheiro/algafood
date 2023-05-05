package com.apigaworks.algafood.domain.dto.restaurante;

import com.apigaworks.algafood.domain.dto.cozinha.CozinhaDTO;
import com.apigaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class RestauranteDTO {

    @Autowired
    private ModelMapper modelMapper;

   private Long id;

    private String nome;

    private BigDecimal taxaFrete;

    private CozinhaDTO cozinha;



    public RestauranteDTO(Restaurante data) {
        modelMapper.map(data, this);
    }
}

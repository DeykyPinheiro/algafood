package com.apigaworks.algafood.domain.dto.cozinha;

import com.apigaworks.algafood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class CozinhaDTO {

    @Autowired
    private ModelMapper modelMapper;

    private Long id;
    private String nome;

    public CozinhaDTO(Cozinha data) {
        // estou chamando cada propriedade e passando o valor pra ela
        modelMapper.map(data, this);
    }
}

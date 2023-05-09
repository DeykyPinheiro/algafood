package com.apigaworks.algafood.domain.dto.endereco;

import com.apigaworks.algafood.domain.dto.cidade.CidadeDto;
import com.apigaworks.algafood.domain.dto.cidade.CidadeUpdateDto;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Endereco;

public record EndecoUpdateDto(

        String cep,

        String logradouro,

        String numero,

        String complemento,

        String bairro,

        CidadeUpdateDto cidade) {

    public EndecoUpdateDto(Endereco endereco) {
        this(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(),
                new CidadeUpdateDto(endereco.getCidade()));
    }

}

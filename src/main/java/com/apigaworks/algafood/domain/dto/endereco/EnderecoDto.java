package com.apigaworks.algafood.domain.dto.endereco;

import com.apigaworks.algafood.domain.dto.cidade.CidadeDto;
import com.apigaworks.algafood.domain.model.Endereco;

public record EnderecoDto(

        String cep,

        String logradouro,

        String numero,

        String complemento,

        String bairro,

        CidadeDto cidade) {

    public EnderecoDto(Endereco endereco) {
        this(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getBairro(), new CidadeDto(endereco.getCidade()));
    }

    public EnderecoDto {
    }
}

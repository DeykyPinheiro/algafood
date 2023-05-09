package com.apigaworks.algafood.domain.dto.endereco;

import com.apigaworks.algafood.domain.dto.cidade.CidadeDto;
import com.apigaworks.algafood.domain.model.Endereco;

public record EnderecoDto(

        String cep,

        String logradouro,

        String numero,

        String complemento,

        String bairro,

        String nomeCidade,

        String nomeEstado) {

    public EnderecoDto(Endereco data) {
        this(data.getCep(), data.getLogradouro(), data.getNumero(), data.getComplemento(),
                data.getBairro(), data.getCidade().getNome(), data.getCidade().getEstado().getNome());
    }
}

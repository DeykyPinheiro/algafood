package com.apigaworks.algafood.domain.dto.endereco;

import com.apigaworks.algafood.domain.dto.cidade.CidadeDto;
import com.apigaworks.algafood.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;

public record EnderecoDto(

        @Schema(example = "01001001")
        String cep,

        @Schema(example = "Rua Oscar Freire")
        String logradouro,

        @Schema(example = "123")
        String numero,

        @Schema(example = "Apartamento")
        String complemento,

        @Schema(example = "Liberdade")
        String bairro,

        CidadeDto cidade) {

    public EnderecoDto(Endereco endereco) {
        this(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(),
                endereco.getBairro(), new CidadeDto(endereco.getCidade()));
    }

    public EnderecoDto {
    }
}

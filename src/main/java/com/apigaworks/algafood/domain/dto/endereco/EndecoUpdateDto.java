package com.apigaworks.algafood.domain.dto.endereco;

import com.apigaworks.algafood.domain.dto.cidade.CidadeDto;
import com.apigaworks.algafood.domain.dto.cidade.CidadeUpdateDto;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EndecoUpdateDto(

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

        @Valid
        @NotNull
        CidadeUpdateDto cidade) {

    public EndecoUpdateDto(Endereco endereco) {
        this(endereco.getCep(), endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(),
                new CidadeUpdateDto(endereco.getCidade()));
    }

}

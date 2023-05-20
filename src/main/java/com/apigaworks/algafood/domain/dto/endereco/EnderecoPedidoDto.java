package com.apigaworks.algafood.domain.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoPedidoDto(

        @NotBlank
        String cep,

        @NotBlank
        String logradouro,

        @NotBlank
        String numero,

        String complemento,

        @NotBlank
        String bairro,

        @NotNull
        Long cidadeId
) {
}

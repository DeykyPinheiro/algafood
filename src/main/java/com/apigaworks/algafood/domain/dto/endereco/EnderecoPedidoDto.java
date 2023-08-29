package com.apigaworks.algafood.domain.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoPedidoDto(

        @Schema(example = "01001001")
        @NotBlank
        String cep,

        @Schema(example = "Rua Oscar Freire")
        @NotBlank
        String logradouro,

        @Schema(example = "123")
        @NotBlank
        String numero,

        @Schema(example = "Apartamento")
        String complemento,

        @NotBlank
        String bairro,

        @Schema(example = "1")
        @NotNull
        Long cidadeId
) {
}

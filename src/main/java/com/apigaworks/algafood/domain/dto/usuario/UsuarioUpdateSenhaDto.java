package com.apigaworks.algafood.domain.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateSenhaDto(

        @NotBlank
        @Schema(example = "123")
        String senhaAtual,

        @NotBlank
        @Schema(example = "321")
        String novaSenha
) {
}

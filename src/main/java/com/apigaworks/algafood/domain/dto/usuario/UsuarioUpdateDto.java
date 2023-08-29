package com.apigaworks.algafood.domain.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDto(

        @NotBlank
        @Schema(example = "jorge")
        String nome,

        @Email
        @NotBlank
        @Schema(example = "jorge@algafood.com")
        String email
) {
}

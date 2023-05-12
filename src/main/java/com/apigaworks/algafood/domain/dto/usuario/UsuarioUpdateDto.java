package com.apigaworks.algafood.domain.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDto(

        @NotBlank
        String nome,

        @Email
        @NotBlank
        String email
) {
}

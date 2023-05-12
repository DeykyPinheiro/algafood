package com.apigaworks.algafood.domain.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateSenhaDto(

        @NotBlank
        String senhaAtual,

        @NotBlank
        String novaSenha
) {
}

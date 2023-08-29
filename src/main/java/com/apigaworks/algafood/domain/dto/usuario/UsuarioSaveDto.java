package com.apigaworks.algafood.domain.dto.usuario;

import com.apigaworks.algafood.domain.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioSaveDto(
        @NotBlank
        @Schema(example = "jorge")
        String nome,

        @NotBlank
        @Email
        @Schema(example = "jorge@algafood.com")
        String email,

        @NotBlank
        @Schema(example = "123")
        String senha) {
    public UsuarioSaveDto(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}

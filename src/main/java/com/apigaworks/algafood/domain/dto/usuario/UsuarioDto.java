package com.apigaworks.algafood.domain.dto.usuario;

import com.apigaworks.algafood.domain.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDto(

        @Schema(example = "1")
        Long id,

        @Schema(example = "jorge")
        String nome,

        @Schema(example = "jorge@algafood.com")
        String email
) {
    public UsuarioDto(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}

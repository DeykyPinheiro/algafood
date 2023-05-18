package com.apigaworks.algafood.domain.dto.usuario;

import com.apigaworks.algafood.domain.model.Usuario;
import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

public record UsuarioListDto(
        Long id,

        String nome,

        String email
) {

    public UsuarioListDto(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public static List<UsuarioListDto> converterLista(Collection<Usuario> listaUsuario) {
        return  listaUsuario.stream().map(UsuarioListDto::new).collect(Collectors.toList());
    }
}

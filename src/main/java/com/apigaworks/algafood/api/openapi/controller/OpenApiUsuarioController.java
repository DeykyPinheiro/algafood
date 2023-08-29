package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.grupo.GrupoListDto;
import com.apigaworks.algafood.domain.dto.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "security_auth") // colocar isso em uma interface que vai ser implementada por um endpoint
@Tag(name = "Usuarios")
public interface OpenApiUsuarioController {

    @Operation(summary = "Salva um usuario")
    UsuarioDto salvar(@RequestBody(description = "representacao de atualizacoes de usuario") UsuarioSaveDto usuario);

    @Operation(summary = "Lista Usuarios")
    List<UsuarioListDto> listar();

    @Operation(summary = "Busca um Usuario por Id")
    UsuarioDto buscar(@Parameter(description = "Id de usuario", example = "1", required = true) Long id);

    @Operation(summary = "Exclui um usuario por Id")
    void remover(@Parameter(description = "Id de usuario", example = "1", required = true) Long id);

    @Operation(summary = "Atuaaliza um usuario")
    UsuarioDto atualizar(@Parameter(description = "Id de usuario", example = "1", required = true) Long id,
                         @RequestBody(description = "representacao de atualizacoes de usuario") UsuarioUpdateDto usuario);

    @Operation(summary = "atualiza apenas a senha do Usuario")
    void atualizarSenha(@Parameter(description = "Id de usuario", example = "1", required = true) Long id,
                        @RequestBody(description = "representacao de atualizacoes de senhas") UsuarioUpdateSenhaDto usuario);

    @Operation(summary = "Lista Usuario por grupo")
    List<GrupoListDto> listarGruposPorUsuario(@Parameter(description = "Id de usuario", example = "1", required = true) Long userId);

    @Operation(summary = "Associa um Usuario a um grupo")
    void associarGrupo(@Parameter(description = "Id de usuario", example = "1", required = true) Long userId,
                       @Parameter(description = "Id de grupo", example = "1", required = true) Long grupoId);

    @Operation(summary = "Desssocia um Usuario de um grupo")
    void desassociarGrupo(@Parameter(description = "Id de usuario", example = "1", required = true) Long userId,
                          @Parameter(description = "Id de grupo", example = "1", required = true) Long grupoId);
}

package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.grupo.GrupoDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoListDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoSaveDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoUpdateDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Grupos")
public interface OpenApiGrupoController {

    @Operation(summary = "Adiciona um novo grupo")
    GrupoDto adicionar(@RequestBody(description = "representacao de uma novo grupo") GrupoSaveDto grupo);

    @Operation(summary = "Lista todos os grupos")
    List<GrupoListDto> listar();

    @Operation(summary = "Busca um grupo por Id")
    GrupoDto buscar(@Parameter(description = "Id do grupo", example = "1", required = true) Long id);

    @Operation(summary = "Atualiza um grupo por Id")
    GrupoDto atualizar(@Parameter(description = "Id do grupo", example = "1", required = true) Long id,
                       @RequestBody(description = "representacao de uma novo grupo") GrupoUpdateDto grupo);

    @Operation(summary = "Remove um grupo por Id")
    void remover(@Parameter(description = "Id do grupo", example = "1", required = true) Long id);

    @Operation(summary = "Lista permissoes do Grupos")
    List<PermissaoListDto> listaPermissoesPorGrupo(@Parameter(description = "Id do grupo", example = "1", required = true) Long grupoId);


    @Operation(summary = "Associa um grupo a Permissoes")
    void associarPermissao(@Parameter(description = "Id do grupo", example = "1", required = true) Long grupoId,
                           @Parameter(description = "Id da permissao ", example = "1", required = true) Long permissaoId);

    @Operation(summary = "Desassocia um grupo de Permissoes")
    void desassociarPermissao(@Parameter(description = "Id do grupo", example = "1", required = true) Long grupoId,
                              @Parameter(description = "Id da permissao", example = "1", required = true) Long permissaoId);
}

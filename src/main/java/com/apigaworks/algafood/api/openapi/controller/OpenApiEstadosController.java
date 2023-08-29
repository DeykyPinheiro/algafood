package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.model.Estado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "security_auth")
@Tag(name = "Estados")
public interface OpenApiEstadosController {

    @Operation(summary = "Lista todos os estados")
    List<Estado> listar();

    @Operation(summary = "Busca Estado por Id")
    Estado listar(@Parameter(description = "Id de um Estado", example = "1", required = true) Long id);

    @Operation(summary = "Salva um Estado")
    Estado salvar(@RequestBody(description = "Novo estado Cadastrado") Estado estado);

    Estado atualizar(@Parameter(description = "Id de um Estado", example = "1", required = true) Long idEstado,
                     @RequestBody(description = "representacao de atualizacoes de Estado") Estado estado);

    @Operation(summary = "Remove um estado")
    void excluir(@Parameter(description = "Id de um Estado", example = "1", required = true) Long id);
}

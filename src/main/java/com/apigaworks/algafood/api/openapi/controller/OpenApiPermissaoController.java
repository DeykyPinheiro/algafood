package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoSaveDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoUpdateDto;
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
@Tag(name = "Permissao")
public interface OpenApiPermissaoController {

    @Operation(summary = "Salva uma permissao")
    PermissaoDto salvar(@RequestBody(description = "representacao de uma nova permissao") PermissaoSaveDto permissaoDto);

    @Operation(summary = "Busca uma permissao por Id")
    PermissaoDto buscar(@Parameter(description = "Id de uma permissao", example = "1", required = true) Long permissaoId);

    @Operation(summary = "Lista todas as permissoes")
    List<PermissaoListDto> listar();

    @Operation(summary = "Atualiza permissoes")
    PermissaoDto atualizar(@Parameter(description = "Id de uma permissao", example = "1", required = true) Long permissaoId,
                           @Parameter(description = "Id de uma permissao", example = "1", required = true) PermissaoUpdateDto permissaoDto);
    @Operation(summary = "Remove uma permissao")
    void remover(@Parameter(description = "Id de uma permissao", example = "1", required = true) Long permissaoId);
}

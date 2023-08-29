package com.apigaworks.algafood.api.openapi.controller;


import com.apigaworks.algafood.domain.model.Cidade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "security_auth") // colocar isso em uma interface que vai ser implementada por um endpoint
@Tag(name = "Cidades")
public interface OpenApiCidadeController {

    @Operation(summary = "Busca uma cidade por Id")
    Cidade buscar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id);

    @Operation(summary = "Lista as Cidades")
    List<Cidade> listar();

    @Operation(summary = "Adiciona uma cidade")
    Cidade adicionar(@RequestBody(description = "representacao de uma nova cidade") Cidade cidade);

    @Operation(summary = "Atualiza uma cidade por Id")
    Cidade atualizar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id,
                     @RequestBody(description = "representacao de atualizacoes de cidade") Cidade cidade);

    @Operation(summary = "Exclui uma cidade por Id")
    void excluir(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id);
}

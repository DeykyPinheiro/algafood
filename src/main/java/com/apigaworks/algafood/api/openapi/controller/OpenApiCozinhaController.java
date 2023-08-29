package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.openapi.PageableParameter;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.model.Cozinha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cozinhas")
public interface OpenApiCozinhaController {



//    isso é um query param abstrai tudo como uma anotacao
//    @Parameter(
//            in = ParameterIn.QUERY,
//            name = "sort",
//            description = "Critério de ordenação: propriedade(asc|desc).",
//            examples = {
//                    @ExampleObject("nome"),
//                    @ExampleObject("nome,asc"),
//                    @ExampleObject("nome,desc")
//            }
//    )
    @Operation(summary = "Lista tipos de cozinhas paginadas")
    @PageableParameter
    Page<Cozinha> listar(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Buscar tipo de Cozinha por Id")
    Cozinha buscar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id);

    @Operation(summary = "Salva uma nova cozinha")
    Cozinha salvar(Cozinha cozinha);

    @Operation(summary = "Atualizar uma cozinha")
    Cozinha atualizar(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id,
                      @RequestBody(description = "representacao de uma cozinha") Cozinha cozinha);

    @Operation(summary = "Remove uma Cozinha")
    void remover(@Parameter(description = "Id de uma cidade", example = "1", required = true) Long id);

    @Operation(summary = "Usado para testar query params")
    List<String> search(String dia, String mes, String ano);

    @Operation(summary = "Primeira cozinha")
    @GetMapping("/primeiro")
    Cozinha buscarPrimeiro();
}

package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Forma De Pagamento")
public interface OpenApiFormaPagamentoController {

    @Operation(summary = "Lista todas formas de pagamento")
    List<FormaPagamentoDto> listar();

    @Operation(summary = "Busca forma de pagamento por Id")
    FormaPagamentoDto buscar(@Parameter(description = "Id de uma forma de pagamento", example = "1", required = true) Long id);

    @Operation(summary = "salva uma formas de pagamento")
    FormaPagamentoDto salvar(@RequestBody(description = "representacao de uma forma de pagamento") FormaPagamentoDto formaPagamento);

    @Operation(summary = "remove uma formas de pagamento")
    void excluir(@Parameter(description = "Id de uma forma de pagamento", example = "1", required = true) long id);
}

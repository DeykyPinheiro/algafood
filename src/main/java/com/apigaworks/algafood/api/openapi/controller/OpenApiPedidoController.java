package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.openapi.PageableParameter;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.filter.PedidoFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pedidos")
public interface OpenApiPedidoController {

    @Operation(
            summary = "Pesquisa os pedidos",
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "clienteId",
                            description = "ID do cliente para filtro da pesquisa",
                            example = "1", schema = @Schema(type = "integer")),
                    @Parameter(in = ParameterIn.QUERY, name = "restauranteId",
                            description = "ID do restaurante para filtro da pesquisa",
                            example = "1", schema = @Schema(type = "integer")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoInicio",
                            description = "Data/hora de criação inicial para filtro da pesquisa",
                            example = "2019-12-01T00:00:00Z", schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(in = ParameterIn.QUERY, name = "dataCriacaoFim",
                            description = "Data/hora de criação final para filtro da pesquisa",
                            example = "2019-12-02T23:59:59Z", schema = @Schema(type = "string", format = "date-time"))
            })
    Page<PedidoListDto> buscar(PedidoFilter pedidoFilter, @Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Busca pedido por id")
    PedidoDto buscar(@Parameter(description = "Id de um pedido", example = "1", required = true) Long pedidoId);

    @Operation(summary = "Salva um pedido")
    PedidoDto salvar(@RequestBody(description = "representacao de uma novo pedido") PedidoSaveDto pedidoDto);

    @Operation(summary = "Muda o status de um pedido para CONFIRMADO")
    void confirmarPedido(@Parameter(description = "Id de um pedido", example = "1", required = true) Long pedidoId) throws MessagingException;

    @Operation(summary = "Muda o status de um pedido para ENTREGUE")
    void entregarPedido(@Parameter(description = "Id de um pedido", example = "1", required = true) Long pedidoId);

    @Operation(summary = "Muda o status de um pedido para CANCELADO")
    void cancelarPedido(@Parameter(description = "Id de um pedido", example = "1", required = true) Long pedidoId);
}

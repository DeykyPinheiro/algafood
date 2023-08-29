package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Estatisticas")
public interface OpenApiEstatisticaController {
    //    isso aqui é chamado quando o user aceita application/json

    @Operation(summary = "Pesquisa por venda diarias")
    List<VendaDiaria> consulVendaDiarias(VendaDiariaFilter filter);

    ResponseEntity<byte[]> consulVendaDiariasPdf(VendaDiariaFilter filter) throws JRException;
}

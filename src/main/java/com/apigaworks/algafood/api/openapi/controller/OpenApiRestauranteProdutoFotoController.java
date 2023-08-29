package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import com.apigaworks.algafood.domain.model.FotoProduto;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Fotos")
public interface OpenApiRestauranteProdutoFotoController {


    @Operation(summary = "Atualizao uma Foto")
    FotoProduto atualizarFoto(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                              @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId,
                              @RequestBody(description = "foto") FotoDto arquivoDto) throws IOException;

//    @Operation(summary = "Busca a foto do produto de um restaurante", responses = {
//            @ApiResponse(responseCode = "200", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = FotoProduto.class)),
//                    @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
//                    @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
//            })
//    })
    FotoProduto buscar(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                       @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId);

    @Operation(summary = "Devolve a  uma Foto binaria")
    ResponseEntity<InputStreamResource> buscarBinarioImage(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                                                           @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId,
                                                           @RequestHeader(name = "accept") String acceptHeadder) throws HttpMediaTypeNotAcceptableException;

    default void verificarMediaTypeAceitas(MediaType mediaTypeFoto,
                                           List<MediaType> listaMediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
        //        aqui ele compara se qualquer uma estiver na lista ele vai returnar verdadeiro
        Boolean compativel = listaMediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        //        to usando o construtor onde coloco a lista de MediaTypes aceitas
        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(listaMediaTypesAceitas);
        }
    }

    @Operation(summary = "Excluir uma Foto")
    void deletarFoto(
            @Parameter(description = "Id do produto", example = "1", required = true) Long restauranteId,
            @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId);
}

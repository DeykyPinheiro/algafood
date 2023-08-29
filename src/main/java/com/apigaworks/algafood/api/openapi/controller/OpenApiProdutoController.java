package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoListDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoUpdateDto;
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
@Tag(name = "Produtos")
public interface OpenApiProdutoController {

    @Operation(summary = "Salva um produtos Relacionado a um Restaurante")
    ProdutoDto salvar(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                      @RequestBody(description = "representacao de uma novo produto") ProdutoSaveDto produto);

    @Operation(summary = "Lista os Produtos de um restaurante")
    List<ProdutoListDto> listar(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
//        @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos); // <-- ORIGINAL, NAO SEI DOCUMENTAR AINDA, DEPOIS DOCUMENTA FILHO DA PUTA
                                @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos);

    @Operation(summary = "Atualiza o produto de um restaurante")
    ProdutoDto atualizar(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                         @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId,
                         @RequestBody(description = "representacao de uma novo produto") ProdutoUpdateDto produto);

    @Operation(summary = "Remove um produto de um restaurante")
    void remover(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                 @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId);

    @Operation(summary = "Busca por produto")
    ProdutoDto buscarProdutoPorId(@Parameter(description = "Id do restaurante", example = "1", required = true) Long restauranteId,
                                  @Parameter(description = "Id do produto", example = "1", required = true) Long produtoId);
}

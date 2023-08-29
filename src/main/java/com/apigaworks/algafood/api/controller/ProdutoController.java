package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiProdutoController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoListDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoUpdateDto;
import com.apigaworks.algafood.domain.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class ProdutoController implements OpenApiProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    public ProdutoDto salvar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoSaveDto produto) {
        return produtoService.salvar(restauranteId, produto);
    }

    //    RequestParam é quando vc passa no caminho depois de "?"
    @Override
    @GetMapping
    @CheckSecurity.Restaurantes.PodeConsultar
    public List<ProdutoListDto> listar(@PathVariable Long restauranteId,
                                       @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
        return produtoService.listarPorId(restauranteId, incluirInativos);
    }

    @Override
    @PutMapping("/{produtoId}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public ProdutoDto atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoUpdateDto produto) {
        return produtoService.atualizar(restauranteId, produtoId, produto);
    }

    @Override
    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public void remover(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        produtoService.remover(restauranteId, produtoId);
    }

    //    consulta mais diferente, pq tenho que relacionar produto e restaurante
    @Override
    @GetMapping("/{produtoId}")
    public ProdutoDto buscarProdutoPorId(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        return produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
    }
}

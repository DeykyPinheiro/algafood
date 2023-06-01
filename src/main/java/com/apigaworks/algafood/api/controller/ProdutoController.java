package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoListDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoUpdateDto;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDto salvar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoSaveDto produto) {
        return produtoService.salvar(restauranteId, produto);
    }

    //    RequestParam é quando vc passa no caminho depois de "?"
    @GetMapping
    public List<ProdutoListDto> listar(@PathVariable Long restauranteId,
                                       @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
        return produtoService.listarPorId(restauranteId, incluirInativos);
    }

    @PutMapping("/{produtoId}")
    public ProdutoDto atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoUpdateDto produto) {
        return produtoService.atualizar(restauranteId, produtoId, produto);
    }

    @DeleteMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        produtoService.remover(restauranteId, produtoId);
    }

    //    consulta mais diferente, pq tenho que relacionar produto e restaurante
    @GetMapping("/{produtoId}")
    public ProdutoDto buscarProdutoPorId(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        return produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
    }
}

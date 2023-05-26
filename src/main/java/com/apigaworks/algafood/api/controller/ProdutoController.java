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
@RequestMapping("/restaurantes/{idRestaurante}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoDto salvar(@PathVariable Long idRestaurante, @RequestBody @Valid ProdutoSaveDto produto) {
        return produtoService.salvar(idRestaurante, produto);
    }

    //    RequestParam é quando vc passa no caminho depois de "?"
    @GetMapping
    public List<ProdutoListDto> listar(@PathVariable Long idRestaurante, @RequestParam(required = false) Boolean incluirInativos) {
        if (incluirInativos == null) {
            incluirInativos = false;
        }
        return produtoService.listarPorId(idRestaurante, incluirInativos);
    }

    @PutMapping("/{idProduto}")
    public ProdutoDto atualizar(@PathVariable Long idRestaurante, @PathVariable Long idProduto, @RequestBody @Valid ProdutoUpdateDto produto) {
        return produtoService.atualizar(idRestaurante, idProduto, produto);
    }

    @DeleteMapping("/{idProduto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long idRestaurante, @PathVariable Long idProduto) {
        produtoService.remover(idRestaurante, idProduto);
    }

    //    consulta mais diferente, pq tenho que relacionar produto e restaurante
    @GetMapping("/{idProduto}")
    public ProdutoDto buscarProdutoPorId(@PathVariable Long idRestaurante, @PathVariable Long idProduto) {
        return produtoService.buscarProdutoPorIdPorRestaurante(idRestaurante, idProduto);
    }


}

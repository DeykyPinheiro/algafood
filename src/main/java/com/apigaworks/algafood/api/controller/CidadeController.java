package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {


    @Autowired
    private CidadeService cidadeService;

    @GetMapping("/{id}")
    public Cidade buscar(@PathVariable Long id) {
        return cidadeService.buscarOuFalhar(id);
    }

    @GetMapping
    public List<Cidade> listar() {
        return cidadeService.listar();
    }

    @PostMapping
    public Cidade adicionar(@RequestBody Cidade cidade) {
        cidade = cidadeService.salvar(cidade);
        return cidade;
    }

//    padrao na hora de atualizar é receber o id no path e os
//    dados a serem atualizados todos no body
    @PutMapping("/{id}")
    public Cidade atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {
        cidade = cidadeService.atualizar(id, cidade);
        return cidade;
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        cidadeService.remover(id);
    }
}

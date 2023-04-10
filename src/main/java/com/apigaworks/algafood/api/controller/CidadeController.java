package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Cidade;
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
}

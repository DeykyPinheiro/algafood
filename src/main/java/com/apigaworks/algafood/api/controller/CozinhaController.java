package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.service.CozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaService cozinhaService;

    @GetMapping
    public List<Cozinha> listar() {
        List<Cozinha> a = cozinhaService.listar();
        return a;
    }

    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id) {
        return cozinhaService.buscarPorId(id);
    }

    @PostMapping
    public Cozinha salvar(@RequestBody Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @PutMapping
    public Cozinha atualizar(@RequestBody Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cozinhaService.remover(id);
    }
}

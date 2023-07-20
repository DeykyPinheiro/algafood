package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadosController {


    @Autowired
    private EstadoService estadoService;

    @GetMapping
    @CheckSecurity.Estado.PodeConsultar
    public List<Estado> listar() {
        return estadoService.listar();
    }

    @GetMapping("/{id}")
    @CheckSecurity.Estado.PodeConsultar
    public Estado listar(@PathVariable Long id) {
        return estadoService.buscarOuFalhar(id);
    }

    @PostMapping
    @CheckSecurity.Estado.PodeEditar
    public Estado salvar(@RequestBody Estado estado) {
        return estadoService.salvar(estado);
    }

    @PutMapping("/{idEstado}")
    @CheckSecurity.Estado.PodeEditar
    public Estado atualizar(@PathVariable Long idEstado, @RequestBody Estado estado) {
        return estadoService.atualizar(idEstado, estado);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.Estado.PodeEditar
    public void excluir(@PathVariable Long id) {
        estadoService.remover(id);
    }
}

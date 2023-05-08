package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDTO;
import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formaPagamentos")
public class FormaPagamentoController {


    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @GetMapping
    public List<FormaPagamentoDTO> listar(){
        return formaPagamentoService.listar();
    }

    @GetMapping("/{id}")
    public FormaPagamentoDTO buscar(@PathVariable  Long id){
        return formaPagamentoService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoDTO salvar(@RequestBody FormaPagamentoDTO formaPagamento){
        return formaPagamentoService.salvar(formaPagamento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id){
        formaPagamentoService.excluir(id);
    }


}

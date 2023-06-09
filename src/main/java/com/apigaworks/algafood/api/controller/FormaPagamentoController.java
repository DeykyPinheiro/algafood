package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
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
    public List<FormaPagamentoDto> listar(){
        return formaPagamentoService.listar();
    }

    @GetMapping("/{id}")
    public FormaPagamentoDto buscar(@PathVariable  Long id){
        return formaPagamentoService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoDto salvar(@RequestBody FormaPagamentoDto formaPagamento){
        return formaPagamentoService.salvar(formaPagamento);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id){
        formaPagamentoService.excluir(id);
    }


}

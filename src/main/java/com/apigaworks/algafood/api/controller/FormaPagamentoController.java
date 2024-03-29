package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiFormaPagamentoController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formaPagamentos")
public class FormaPagamentoController implements OpenApiFormaPagamentoController {


    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Override
    @GetMapping
    @CheckSecurity.FormaPagamento.PodeConsultar
    public List<FormaPagamentoDto> listar(){
        return formaPagamentoService.listar();
    }

    @Override
    @GetMapping("/{id}")
    @CheckSecurity.FormaPagamento.PodeConsultar
    public FormaPagamentoDto buscar(@PathVariable Long id){
        return formaPagamentoService.buscarOuFalhar(id);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckSecurity.FormaPagamento.PodeEditar
    public FormaPagamentoDto salvar(@RequestBody FormaPagamentoDto formaPagamento){
        return formaPagamentoService.salvar(formaPagamento);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.FormaPagamento.PodeEditar
    public void excluir(@PathVariable long id){
        formaPagamentoService.excluir(id);
    }


}

package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoListDto> listar(){
        return pedidoService.listar();
    }

    @GetMapping("/{pedidoId}")
    public PedidoDto buscar(@PathVariable Long pedidoId){
        return pedidoService.buscarOuFalhar(pedidoId);
    }
}

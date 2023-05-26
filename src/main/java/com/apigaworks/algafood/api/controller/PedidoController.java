package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.repository.filter.PedidoFilter;
import com.apigaworks.algafood.domain.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
//    só de por ali no campo o spring ja gerencia e pega da requestParam
    public Page<PedidoListDto> buscar(PedidoFilter pedidoFilter, Pageable pageable) {
        return pedidoService.buscar(pedidoFilter, pageable);
    }

    @GetMapping("/{pedidoId}")
    public PedidoDto buscar(@PathVariable Long pedidoId) {
        return pedidoService.buscarOuFalhar(pedidoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoDto salvar(@RequestBody @Valid PedidoSaveDto pedidoDto) {
        return pedidoService.salvar(pedidoDto);
    }

    @PutMapping("/{pedidoId}/confirmacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmarPedido(@PathVariable Long pedidoId) {
        pedidoService.confirmarPedido(pedidoId);
    }

    @PutMapping("/{pedidoId}/entrega")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void entregarPedido(@PathVariable Long pedidoId) {
        pedidoService.entregarPedido(pedidoId);
    }

    @PutMapping("/{pedidoId}/cancelamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarPedido(@PathVariable Long pedidoId) {
        pedidoService.cancelarPedido(pedidoId);
    }



}

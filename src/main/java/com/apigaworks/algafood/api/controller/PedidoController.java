package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiPedidoController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.filter.PedidoFilter;
import com.apigaworks.algafood.domain.service.PedidoService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController implements OpenApiPedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Override
    @GetMapping
//    @CheckSecurity.Pedidos.PodePesquisar
//    só de por ali no campo o spring ja gerencia e pega da requestParam
    @CheckSecurity.Pedidos.PodeBuscar
    public Page<PedidoListDto> buscar(PedidoFilter pedidoFilter, Pageable pageable) {
        return pedidoService.buscar(pedidoFilter, pageable);
    }

    @Override
    @GetMapping("/{pedidoId}")
    @CheckSecurity.Pedidos.PodeBuscar
    public PedidoDto buscar(@PathVariable Long pedidoId) {
        return pedidoService.buscarOuFalhar(pedidoId);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
        @CheckSecurity.Pedidos.PodeGerenciarPedido
    public PedidoDto salvar(@RequestBody @Valid PedidoSaveDto pedidoDto) {
        return pedidoService.salvar(pedidoDto);
    }

    @Override
    @PutMapping("/{pedidoId}/confirmacao")
    @CheckSecurity.Pedidos.PodeGerenciarPedido
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmarPedido(@PathVariable Long pedidoId) throws MessagingException {
        pedidoService.confirmarPedido(pedidoId);
    }

    @Override
    @PutMapping("/{pedidoId}/entrega")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckSecurity.Pedidos.PodeGerenciarPedido
    public void entregarPedido(@PathVariable Long pedidoId) {
        pedidoService.entregarPedido(pedidoId);
    }

    @Override
    @PutMapping("/{pedidoId}/cancelamento")
    @CheckSecurity.Pedidos.PodeGerenciarPedido
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarPedido(@PathVariable Long pedidoId) {
        pedidoService.cancelarPedido(pedidoId);
    }



}

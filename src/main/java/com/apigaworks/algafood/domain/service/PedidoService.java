package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.exception.PedidoNaoEncontratoException;
import com.apigaworks.algafood.domain.exception.UsuarioNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Pedido;
import com.apigaworks.algafood.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }


    public List<PedidoListDto> listar() {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        return PedidoListDto.converterLista(listaPedidos);
    }

    public PedidoListDto buscarOuFalhar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontratoException(pedidoId));
        return new PedidoListDto(pedido);
    }
}

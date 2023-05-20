package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.apigaworks.algafood.domain.exception.PedidoNaoEncontratoException;
import com.apigaworks.algafood.domain.exception.UsuarioNaoEncontratoException;
import com.apigaworks.algafood.domain.model.*;
import com.apigaworks.algafood.domain.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PrimitiveIterator;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private CidadeService cidadeService;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }


    public List<PedidoListDto> listar() {
        List<Pedido> listaPedidos = pedidoRepository.findAll();
        return PedidoListDto.converterLista(listaPedidos);
    }

    public PedidoDto buscarOuFalhar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontratoException(pedidoId));
        return new PedidoDto(pedido);
    }

    @Transactional
    public PedidoDto salvar(PedidoSaveDto pedidoDto) {

        Pedido pedido = new Pedido(pedidoDto);

//      uso o cliente 1 pq  vou mudar mais pra fente
        Usuario cliente = usuarioRepository.findById(usuarioService.buscarOuFalhar(1L).id()).get();

        Restaurante restaurante = restauranteService.buscarOuFalhar(pedidoDto.restauranteId());
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(formaPagamentoService.buscarOuFalhar(pedidoDto.formaPagamentoId()).id()).get();
        Cidade cidade = cidadeService.buscarPorId(pedidoDto.enderecoEntrega().cidadeId());


        if (!restaurante.ehFormaPagamentoAceita(formaPagamento)) {
            throw new NegocioException("meio de pagamento nao aceito pelo restaurante");
        }


        return null;

    }
}

//-[] converte o que recebeu para pedido
//-[] valida restaurante
//-[] valida forma de pagamento
//-[] valido endereco de entrega
//-[] valido items por restaurante
//-[] mensagens de erro

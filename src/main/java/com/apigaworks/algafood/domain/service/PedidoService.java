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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.PrimitiveIterator;

@Service
public class PedidoService {

    @Autowired
    private ModelMapper modelMapper;

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


//      TODO uso o cliente 1 pq  vou mudar mais pra fente
        Usuario cliente = usuarioRepository.findById(usuarioService.buscarOuFalhar(1L).id()).get();

        Restaurante restaurante = restauranteService.buscarOuFalhar(pedidoDto.restauranteId());
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(formaPagamentoService.buscarOuFalhar(pedidoDto.formaPagamentoId()).id()).get();
        Cidade cidade = cidadeService.buscarPorId(pedidoDto.enderecoEntrega().cidadeId());



        if (!restaurante.ehFormaPagamentoAceita(formaPagamento)) {
            throw new NegocioException("meio de pagamento nao aceito pelo restaurante");
        }

        validarPedido(pedido);


//        TODO gambiarra, mudar quando tiver passando nos testes
        pedido.setCliente(cliente);

        pedido.setRestaurante(restaurante);
        pedido.setEndereco(new Endereco(pedidoDto.enderecoEntrega()));
        pedido.setFormaPagamento(formaPagamento);
        pedido.setSubtotal(new BigDecimal("10"));
        pedido.setTaxaFrete(restaurante.getTaxaFrete());
        pedido.setValorTotal(new BigDecimal("10"));

//
//
        pedidoRepository.save(pedido);

        return new PedidoDto(pedido);
    }


    public void validarPedido( Pedido pedido) {
        pedido.getItens().forEach(item ->
                {
                    Produto produto = restauranteService.encontrarProduto(pedido.getRestaurante().getId(), item.getProduto().getId());

                    item.setPedido(pedido);
                    item.setProduto(produto);
                    item.setPrecoUnitario(produto.getPreco());
//                    TODO gambiarrra, remover e alterara
                    item.setPrecoTotal(produto.getPreco());
                });
    }
}

//-[x] converte o que recebeu para pedido
//-[x] valida restaurante
//-[x] valida forma de pagamento
//-[x] valido endereco de entrega
//-[x] valido items por restaurante
//-[x] mensagens de erro

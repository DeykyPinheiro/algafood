package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoListDto;
import com.apigaworks.algafood.domain.dto.pedido.PedidoSaveDto;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.apigaworks.algafood.domain.exception.PedidoNaoEncontratoException;
import com.apigaworks.algafood.domain.model.*;
import com.apigaworks.algafood.domain.repository.*;
import com.apigaworks.algafood.domain.filter.PedidoFilter;
import com.apigaworks.algafood.infrastructure.spec.PedidoSpecs;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    EnvioEmailService envioEmailService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }


    public Page<PedidoListDto> buscar(PedidoFilter filter, Pageable pageable) {
        Page<Pedido> paginaPedido = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filter), pageable);
        List<PedidoListDto> listaPedidos = PedidoListDto.converterLista(paginaPedido.getContent());
        return new PageImpl<>(listaPedidos, pageable, paginaPedido.getTotalElements());
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
        pedido.setCliente(cliente);
        pedido.setTaxaFrete(restaurante.getTaxaFrete());
        pedido.calcularValorTotal();


        pedidoRepository.save(pedido);

        return new PedidoDto(pedido);
    }


    public void validarPedido(Pedido pedido) {
        pedido.getItens().forEach(item ->
        {
            Produto produto = restauranteService.encontrarProduto(pedido.getRestaurante().getId(), item.getProduto().getId());

            item.setPedido(pedido);
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco());
        });
    }

    @Transactional
    public void confirmarPedido(Long pedidoId) throws MessagingException {
        Pedido pedido = pedidoRepository.findById(buscarOuFalhar(pedidoId).id()).get();
        pedido.confirmarPedido();


//        aqui ele é chamado para dispar a publicacao do pedido, que só é dada quando
//        o objeto é salvo, mas o spring data requer, entao chamamos
        pedidoRepository.save(pedido);

//        enviar e-mail
//        var mensagem = EnvioEmailService.Mensagem.builder()
//                .assunto(pedido.getRestaurante().getNome() + " - pedido confirmado")
//                .corpo("O pedido de codigo <strong> " + pedido.getId() + "</strong> foi confirmado")
//                .destinatario(pedido.getCliente().getEmail())
//                .build();
//
//        envioEmailService.enviar(mensagem);
    }

    @Transactional
    public void entregarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(buscarOuFalhar(pedidoId).id()).get();
        pedido.entregarPedido();
    }

    @Transactional
    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(buscarOuFalhar(pedidoId).id()).get();
        pedido.cancelarPedido();

        //        aqui ele é chamado para dispar a publicacao do pedido, que só é dada quando
//        o objeto é salvo, mas o spring data requer, entao chamamos
        pedidoRepository.save(pedido);
    }
}

//-[x] converte o que recebeu para pedido
//-[x] valida restaurante
//-[x] valida forma de pagamento
//-[x] valido endereco de entrega
//-[x] valido items por restaurante
//-[x] mensagens de erro

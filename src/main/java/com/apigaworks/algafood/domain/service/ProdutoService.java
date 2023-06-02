package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoListDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoSaveDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoUpdateDto;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.ProdutoNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProdutoService {

    private static final String MSG_PRODUTO_EM_USO
            = "Produto de código %d não pode ser removido, pois está em uso";


    private ProdutoRespository produtoRespository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public ProdutoService(ProdutoRespository produtoRespository) {
        this.produtoRespository = produtoRespository;
    }

    @Transactional
    public ProdutoDto salvar(Long restauranteId, @Valid ProdutoSaveDto produtoDto) {
        Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);
        Produto produto = produtoRespository.save(new Produto(produtoDto));
        produto.setRestaurante(restaurante);

//        esse adiciona na lista, mas nao sei muito bem qual
//        o efeito disso ainda
//        restaurante.adicionarProduto(produto);
        return new ProdutoDto(produto);
    }


    public List<ProdutoListDto> listarPorId(Long restauranteId, Boolean incluirInativos) {
        Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

        Collection<Produto> listaProdutos;
        if (incluirInativos == true) {
            listaProdutos = restaurante.getListaProdutos();
        } else {
            listaProdutos = produtoRespository.findAtivosByRestaurante(restaurante);
        }


        return ProdutoListDto.converterLista(listaProdutos);
    }

    @Transactional
    public ProdutoDto atualizar(Long restauranteId, Long produtoId, ProdutoUpdateDto produtoDto) {

        restauranteService.buscarOuFalhar(restauranteId);
        ProdutoDto produtoAtualDto = buscarProdutoPorId(restauranteId, produtoId);
        Produto produtoAtual = produtoRespository.findById(produtoAtualDto.id()).get();


//        nao seja burro, Long pode setar null
//        long seta 0 e isso fode com voce, sempre user Long
        Produto atualizacoes = new Produto(produtoDto);

        modelMapper.map(atualizacoes, produtoAtual);
        return new ProdutoDto(produtoAtual);
    }

    public ProdutoDto buscarProdutoPorId(Long restauranteId, Long produtoId) {
        restauranteService.buscarOuFalhar(restauranteId);
        Produto produto = produtoRespository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontratoException(produtoId));
        return new ProdutoDto(produto);
    }

    public ProdutoDto buscarProdutoPorIdPorRestaurante(Long restauranteId, Long produtoId) {

//        verifico se os dois id existem, tanto produto, quando restaurante

        restauranteService.buscarOuFalhar(restauranteId);
        this.buscarProdutoPorId(restauranteId, produtoId);

//            caso existam, eu vou usar uma funcao que eu mesmo implemento por meio de uma
//            interface, clica em cima que vai pra ela
        Produto produto = produtoRespository.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontratoException(restauranteId, produtoId));

        return new ProdutoDto(produto);

    }

    @Transactional
    public void remover(Long restauranteId, Long produtoId) {
        try {
            restauranteService.buscarOuFalhar(restauranteId);
            this.buscarProdutoPorId(restauranteId, produtoId);
            Produto produto = new Produto(buscarProdutoPorIdPorRestaurante(restauranteId, produtoId));

            produtoRespository.delete(produto);
            produtoRespository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new ProdutoNaoEncontratoException(produtoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PRODUTO_EM_USO, produtoId));
        }
    }

//    public ProdutoDto buscarProdutoPorId(Long restauranteId, Long produtoId) {
//        produtoRespository.buscarProdutoPorId(restauranteId, produtoId);
//        return null;
}

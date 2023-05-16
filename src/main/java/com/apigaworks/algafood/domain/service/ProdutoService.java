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
    public ProdutoDto salvar(Long idRestaurante, @Valid ProdutoSaveDto produtoDto) {
        Restaurante restaurante = restauranteService.buscarOuFalhar(idRestaurante);
        Produto produto = produtoRespository.save(new Produto(produtoDto));
        produto.setRestaurante(restaurante);

//        esse adiciona na lista, mas nao sei muito bem qual
//        o efeito disso ainda
//        restaurante.adicionarProduto(produto);
        return new ProdutoDto(produto);
    }


    public List<ProdutoListDto> listarPorId(Long idRestaurante) {
        Restaurante r = restauranteService.buscarOuFalhar(idRestaurante);
        return ProdutoListDto.converterLista(r.getListaProdutos());
    }

    @Transactional
    public ProdutoDto atualizar(Long idRestaurante, Long idProduto, ProdutoUpdateDto produtoDto) {

        restauranteService.buscarOuFalhar(idRestaurante);
        ProdutoDto produtoAtualDto = buscarProdutoPorId(idRestaurante, idProduto);
        Produto produtoAtual = produtoRespository.findById(produtoAtualDto.id()).get();


//        nao seja burro, Long pode setar null
//        long seta 0 e isso fode com voce, sempre user Long
        Produto atualizacoes = new Produto(produtoDto);

        modelMapper.map(atualizacoes, produtoAtual);
        return new ProdutoDto(produtoAtual);
    }

    public ProdutoDto buscarProdutoPorId(Long idRestaurante, Long idProduto) {
        restauranteService.buscarOuFalhar(idRestaurante);
        Produto produto = produtoRespository.findById(idProduto)
                .orElseThrow(() -> new ProdutoNaoEncontratoException(idProduto));
        return new ProdutoDto(produto);
    }

    public ProdutoDto buscarProdutoPorIdPorRestaurante(Long idRestaurante, Long idProduto) {

            restauranteService.buscarOuFalhar(idRestaurante);
            this.buscarProdutoPorId(idRestaurante, idProduto);

            Produto produto = produtoRespository.buscarProdutoPorIdPorRestaurante(idRestaurante, idProduto)
                    .orElseThrow(() -> new ProdutoNaoEncontratoException(idRestaurante, idProduto));

            return new ProdutoDto(produto);

    }

    @Transactional
    public void remover(Long idRestaurante, Long idProduto) {
        try {
            restauranteService.buscarOuFalhar(idRestaurante);
            this.buscarProdutoPorId(idRestaurante, idProduto);
            Produto produto = new Produto(buscarProdutoPorIdPorRestaurante(idRestaurante, idProduto));

            produtoRespository.delete(produto);
            produtoRespository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new ProdutoNaoEncontratoException(idProduto);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PRODUTO_EM_USO, idProduto));
        }
    }

//    public ProdutoDto buscarProdutoPorId(Long idRestaurante, Long idProduto) {
//        produtoRespository.buscarProdutoPorId(idRestaurante, idProduto);
//        return null;
}

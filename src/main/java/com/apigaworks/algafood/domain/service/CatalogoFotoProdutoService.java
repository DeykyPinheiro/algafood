package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.model.FotoProduto;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;

import com.apigaworks.algafood.infrastructure.service.storage.StorageException;
import jakarta.transaction.Transactional;
import jdk.dynalink.NamedOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CatalogoFotoProdutoService {


    private ProdutoRespository produtoRespository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    public CatalogoFotoProdutoService(ProdutoRespository produtoRespository) {
        this.produtoRespository = produtoRespository;
    }

    //    pra esse save funcionar eu tive que implementar um novo seric edentro do mesmo repository,
//    produto no caso
    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRespository.save(produto);
    }

    @Autowired
    private FotoStorageService fotoStorageService;


    @Transactional
    public Produto salvar(Long restauranteId, Long produtoId, FotoDto arquivoDto) throws IOException {
//        verifica os parametros sao validos e busca o produto
        ProdutoDto produtoAtualDto = produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
        Produto produto = produtoRespository.findById(produtoAtualDto.id()).get();
        String novoNomeArquivo = fotoStorageService.gerarNomeArquivo(arquivoDto.arquivo().getOriginalFilename());

//        deleta a foto se existir
        fotoStorageService.remover(produto.getFotoProduto().getNomeArquivo());

//        pega o arquivo
        MultipartFile arquivo = arquivoDto.arquivo();

//        seta os dados da foto embedada
        FotoProduto foto = new FotoProduto();
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(novoNomeArquivo);
        produto.setFotoProduto(foto);

//        isso é feito caso dar algum erro pra salvar ele seja descarregado
//        antes que a foto seja efetivamente armazenada
        produtoRespository.flush();
        FotoStorageService.NovaFoto novaFoto = FotoStorageService.NovaFoto.builder()
                .nomeArquivo(novoNomeArquivo)
                .inputStream(arquivo.getInputStream())
                .build();

        fotoStorageService.armazenar(novaFoto);
        return produto;
    }

    public FotoProduto buscarFoto(Long restauranteId, Long produtoId) {
        ProdutoDto produtoAtualDto = produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
        Produto produto = produtoRespository.findById(produtoAtualDto.id()).get();

        return new FotoProduto(produto);
    }


    public InputStream buscarBinarioImage(Long restauranteId, Long produtoId) {
        ProdutoDto produtoAtualDto = produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
        Produto produto = produtoRespository.findById(produtoAtualDto.id()).get();
        return  fotoStorageService.recuperar(produto.getFotoProduto().getNomeArquivo());
    }
}

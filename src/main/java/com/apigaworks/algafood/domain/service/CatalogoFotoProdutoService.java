package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.model.FotoProduto;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    @Transactional
    public Produto salvar(Long restauranteId, Long produtoId, FotoDto arquivoDto) {
//        verifica os parametros sao validos e busca o produto
        ProdutoDto produtoAtualDto = produtoService.buscarProdutoPorIdPorRestaurante(restauranteId, produtoId);
        Produto produto = produtoRespository.findById(produtoAtualDto.id()).get();

//        pega o arquivo
        MultipartFile arquivo = arquivoDto.arquivo();


//        seta os dados da foto embedada
        FotoProduto foto = new FotoProduto();
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());
        produto.setFotoProduto(foto);

        return produto;
    }
}

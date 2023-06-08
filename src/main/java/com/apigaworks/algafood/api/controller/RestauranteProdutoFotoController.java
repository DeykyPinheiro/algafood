package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import com.apigaworks.algafood.domain.dto.produto.ProdutoDto;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.FotoProduto;
import com.apigaworks.algafood.domain.model.Produto;
import com.apigaworks.algafood.domain.repository.ProdutoRespository;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import com.apigaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.apigaworks.algafood.domain.service.ProdutoService;
import com.apigaworks.algafood.domain.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private ProdutoRespository produtoRespository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteService restauranteService;

    //    MultipartFile esse é o formato que recebemos o binarios
//    MULTIPART_FORM_DATA_VALUE só cai na requisicao se for put e se tiver esse formato
//    posso receber com @requesparam do tipo MULTIPART_FORM_DATA_VALUE ou do jeito que fiz
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProduto atualizarFoto(@PathVariable Long restauranteId,
                                     @PathVariable Long produtoId, @Valid FotoDto arquivoDto) throws IOException {
        return new FotoProduto(catalogoFotoProdutoService.salvar(restauranteId, produtoId, arquivoDto));
    }

    @GetMapping
    public FotoProduto buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        return catalogoFotoProdutoService.buscarFoto(restauranteId, produtoId);
    }

    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
//    InputStreamResource isso serve para servir a foto em uma requisicao
    public ResponseEntity<InputStreamResource> buscarBinarioImage(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
//        o try/catch serve pq é lancada uma exception em catalogoFotoProdutoService.buscarBinarioImage, quando nao é encontrado
//        mas como o formato é errado ele nao é lancado corretamente, com o try/catch funciona
        try {
            InputStream inputStreamFoto = catalogoFotoProdutoService.buscarBinarioImage(restauranteId, produtoId);
//                colocando o fluxo dentro de uma ResponseEntity<InputStreamResource>
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(inputStreamFoto));

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }

    }


}


//tava dentro da funcao atualizar foto, criei pra testar
//    var nomeArquivo = UUID.randomUUID().toString() + "_" + arquivoDto.arquivo().getOriginalFilename();
//
//    var arquivoFoto = Path.of("C:/Users/pinheiro/Downloads/testes_downloadas", nomeArquivo);
//
//        System.out.println(arquivoDto.descricao());
//                System.out.println(arquivoFoto);
//                System.out.println(arquivoDto.arquivo().getContentType());
//
//                try {
//                arquivoDto.arquivo().transferTo(arquivoFoto);
//                } catch (Exception e) {
//                throw new RuntimeException(e);
//                }
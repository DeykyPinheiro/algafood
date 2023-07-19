package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.FotoProduto;
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
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProduto atualizarFoto(@PathVariable Long restauranteId,
                                     @PathVariable Long produtoId, @Valid FotoDto arquivoDto) throws IOException {
        return new FotoProduto(catalogoFotoProdutoService.salvar(restauranteId, produtoId, arquivoDto));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProduto buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        return catalogoFotoProdutoService.buscarFoto(restauranteId, produtoId);
    }

    @GetMapping
//    InputStreamResource isso serve para servir a foto em uma requisicao
    public ResponseEntity<InputStreamResource> buscarBinarioImage(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                                                  @RequestHeader(name = "accept") String acceptHeadder) throws HttpMediaTypeNotAcceptableException {
//        o try/catch serve pq é lancada uma exception em catalogoFotoProdutoService.buscarBinarioImage, quando nao é encontrado
//        mas como o formato é errado ele nao é lancado corretamente, com o try/catch funciona
        InputStream inputStreamFoto = catalogoFotoProdutoService.buscarBinarioImage(restauranteId, produtoId);
        var foto = catalogoFotoProdutoService.buscarFoto(restauranteId, produtoId);

//        pega a media type do arquivo atual, o de baixo lista as medias types compativeis, e verifica se é compativel,
//        se nao estoura uma exception

        MediaType mediaTypeFoto = MediaType.parseMediaType(foto.getContentType());
        List<MediaType> listaMediaTypesAceitas = MediaType.parseMediaTypes(acceptHeadder);
        verificarMediaTypeAceitas(mediaTypeFoto, listaMediaTypesAceitas);

        try {

//                colocando o fluxo dentro de uma ResponseEntity<InputStreamResource>
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(inputStreamFoto));

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }

    }

    private void verificarMediaTypeAceitas(MediaType mediaTypeFoto,
                                           List<MediaType> listaMediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
//        aqui ele compara se qualquer uma estiver na lista ele vai returnar verdadeiro
        Boolean compativel = listaMediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

//        to usando o construtor onde coloco a lista de MediaTypes aceitas
        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(listaMediaTypesAceitas);
        }

    }

    @DeleteMapping
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public void deletarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        catalogoFotoProdutoService.deletarFoto(restauranteId, produtoId);
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
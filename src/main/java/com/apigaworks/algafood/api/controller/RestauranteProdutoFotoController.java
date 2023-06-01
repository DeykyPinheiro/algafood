package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.foto.FotoDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    //    MultipartFile esse é o formato que recebemos o binarios
//    MULTIPART_FORM_DATA_VALUE só cai na requisicao se for put e se tiver esse formato
//    posso receber com @requesparam do tipo MULTIPART_FORM_DATA_VALUE ou do jeito que fiz
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void atualizarFoto(@PathVariable Long restauranteId,
                              @PathVariable Long produtoId, FotoDto arquivoDto)  {

        var nomeArquivo = UUID.randomUUID().toString() + "_" + arquivoDto.arquivo().getOriginalFilename();

        var arquivoFoto = Path.of("C:/Users/pinheiro/Downloads/testes_downloadas", nomeArquivo);

        System.out.println(arquivoDto.descricao());
        System.out.println(arquivoFoto);
        System.out.println(arquivoDto.arquivo().getContentType());

        try {
            arquivoDto.arquivo().transferTo(arquivoFoto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

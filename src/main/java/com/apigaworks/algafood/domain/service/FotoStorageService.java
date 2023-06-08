package com.apigaworks.algafood.domain.service;

import lombok.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface FotoStorageService {
    void armazenar(NovaFoto novaFoto);

//    isso aqui funciona como uma funcao static
    default String gerarNomeArquivo(String nomeOriginal) {
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }


    @Getter
    @Builder
    class NovaFoto {

        String nomeArquivo;

        InputStream inputStream;


    }

}

package com.apigaworks.algafood.domain.service;

import lombok.*;

import java.io.IOException;
import java.io.InputStream;

public interface FotoStorageService {
    void armazenar(NovaFoto novaFoto);


    @Getter
    @Builder
    class NovaFoto {

        String nomeArquivo;

        InputStream inputStream;


    }

}

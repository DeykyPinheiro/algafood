package com.apigaworks.algafood.domain.service;

import java.io.IOException;
import java.io.InputStream;

public interface FotoStorageService {

    void armazenar(NovaFoto novaFoto) throws IOException;

    record NovaFoto(
            String nomeArquivo,

            InputStream inputStream

    ) {
    }
}

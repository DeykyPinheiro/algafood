package com.apigaworks.algafood.infrastructure.service.storage;

import com.apigaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Value("${algafood.storage.local.diretorio-fotos}")
    private Path pathLocal;

    private Path getSaveLocalPath(NovaFoto foto) {
//        isso concatena o nome com a string da variavel
//        return pathLocal.resolve
        return pathLocal.resolve(Path.of(foto.getNomeArquivo()));
//        return Path.of("C:/Users/pinheiro/Downloads/download_testes/", foto.getNomeArquivo());
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            Path arquivoPath = getSaveLocalPath(novaFoto);

//            copia tal qual o transferTo, só que aqui fazemos a conversao e mandando o stream direto
//            para o diretorio local
//            param: daonde copiar, para onde vai
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception ex) {
            throw new StorageException("Nao foi possivel armazenar o arquivo", ex);
        }
    }
}

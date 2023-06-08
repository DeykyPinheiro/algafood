package com.apigaworks.algafood.infrastructure.service.storage;

import com.apigaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Value("${algafood.storage.local.diretorio-fotos}")
    private Path pathLocal;

    private Path getSaveLocalPath(String foto) {
//        isso concatena o nome com a string da variavel
//        return pathLocal.resolve
        return pathLocal.resolve(Path.of(foto));
//        return Path.of("C:/Users/pinheiro/Downloads/download_testes/", foto.getNomeArquivo());
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            Path arquivoPath = getSaveLocalPath(novaFoto.getNomeArquivo());

//            copia tal qual o transferTo, só que aqui fazemos a conversao e mandando o stream direto
//            para o diretorio local
//            param: daonde copiar, para onde vai
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel armazenar o arquivo", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            //        caminho da onde remover
            Path arquivoPath = getSaveLocalPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel excluir o arquivo", e);
        }
    }

    @Override
    public InputStream recuperar(String nomeArquivo) {
        try {
            Path arquivoPath = getSaveLocalPath(nomeArquivo);
            return Files.newInputStream(arquivoPath);
//            meu jeito é o de baixo
//            return new FileInputStream(arquivoPath.toString());
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel gerar um fluxo para o arquivo", e);
        }
    }


}

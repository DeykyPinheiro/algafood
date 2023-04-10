package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CozinhaService {

    public static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha com código %d";
    private CozinhaRepository cozinhaRepository;

    @Autowired
    public CozinhaService(CozinhaRepository cozinhaRepository) {
//        System.out.println("instanciei a porra do repository");
        this.cozinhaRepository = cozinhaRepository;
    }

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    public Cozinha buscarPorId(Long id) {
        return cozinhaRepository.findById(id).get();
    }

    public void remover(Long id) {
        Cozinha c = cozinhaRepository.findById(id).get();
        cozinhaRepository.delete(c);
    }

    public Cozinha atualizar(Cozinha cozinhaNova) {
//        TODO depois arrumar o BeanUtils.copyProperties pq alguns podem ser null
//        TODO e tbm usar um hashmap para receber e converter no objeto recebido
        Cozinha cozinhaAntiga = buscarPorId(cozinhaNova.getId());
        BeanUtils.copyProperties(cozinhaNova, cozinhaAntiga);
        return salvar(cozinhaAntiga);
    }

    public Cozinha buscarOuFalhar(Long id) {
        return cozinhaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MSG_COZINHA_NAO_ENCONTRADA, id)
                ));
    }

    public  Cozinha buscarPrimeiro(){
        return  cozinhaRepository.buscarPrimeiro().get();
    }

}

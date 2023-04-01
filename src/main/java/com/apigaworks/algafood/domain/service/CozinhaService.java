package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CozinhaService {

    private CozinhaRepository cozinhaRepository;

    @Autowired
    public CozinhaService(CozinhaRepository cozinhaRepository) {
        this.cozinhaRepository = cozinhaRepository;
    }

    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.salvar(cozinha);
    }


    public List<Cozinha> listar() {
        return cozinhaRepository.listar();
    }

    public Cozinha buscarPorId(Long id) {
        return cozinhaRepository.buscar(id);
    }

    public void remover(Long id) {
        Cozinha c = cozinhaRepository.buscar(id);
        cozinhaRepository.remover(c);
    }
}

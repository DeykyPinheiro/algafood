package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    private CidadeRepository cidadeRepository;

    @Autowired
    public CidadeService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.salvar(cidade);
    }

    public Cidade buscarPorId(long id) {
        return cidadeRepository.buscar(id);
    }

    public List<Cidade> listar() {
        return cidadeRepository.listar();
    }

    public void remover(long id) {
        Cidade cidade = cidadeRepository.buscar(id);
        cidadeRepository.remover(cidade);
    }
}

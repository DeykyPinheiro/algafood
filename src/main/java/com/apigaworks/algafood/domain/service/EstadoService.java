package com.apigaworks.algafood.domain.service;


import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    private EstadoRepository estadoRepository;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }


    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    public Estado buscar(Long id) {
        return estadoRepository.findById(id).get();
    }

    public void remover(Long id) {
        Estado e = buscar(id);
        estadoRepository.delete(e);
    }
}

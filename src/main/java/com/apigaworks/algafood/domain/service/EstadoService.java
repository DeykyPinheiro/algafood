package com.apigaworks.algafood.domain.service;


import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadoService {

    private EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }


    public Estado salvar(Estado estado) {
        return estadoRepository.salvar(estado);
    }

    public List<Estado> listar() {
        return  estadoRepository.listar();
    }

    public Estado buscar(Long id) {
        return estadoRepository.buscar(id);
    }

    public void remover(Long id) {
        Estado e = buscar(id);
        estadoRepository.remover(e);
    }
}

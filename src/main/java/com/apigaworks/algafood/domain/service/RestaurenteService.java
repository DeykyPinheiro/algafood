package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurenteService {

    private RestauranteRepository restauranteRepository;

    @Autowired
    public RestaurenteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante buscar(Long id) {
        return restauranteRepository.buscar(id);
    }

    public Restaurante salvar(Restaurante restaurante) {
        return restauranteRepository.salvar(restaurante);
    }

    public List<Restaurante> listar() {
        return restauranteRepository.listar();
    }

    public void remover(Long id) {
        Restaurante r = buscar(id);
        restauranteRepository.remover(r);
    }
}

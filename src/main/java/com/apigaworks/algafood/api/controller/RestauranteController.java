package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.RestauranteDTO;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.service.RestauranteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteService.listar();
    }

    @GetMapping("/{id}")
    public RestauranteDTO buscar(@PathVariable Long id) {
//        o recomenaddo é fazer dentro da classe de servico, eu to fazendo aqui pq vou ter que
//        mudar muitas coisa em um projeto didatico, nao vale a pena
        Restaurante r = restauranteService.buscarOuFalhar(id);
        return new RestauranteDTO(r);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante salvar(@RequestBody @Valid Restaurante restaurante) {
        return restauranteService.salvar(restaurante);
    }

    @PutMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {
        return restauranteService.atualizar(id, restaurante);
    }

    //    o argumento HttpServletRequest request, é uma especificacao do jee, o spring injeta automaticamente
    @PatchMapping("/{id}")
    public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> restaurante, HttpServletRequest request) {
        return restauranteService.atualizarParcial(id, restaurante, request);
    }
}

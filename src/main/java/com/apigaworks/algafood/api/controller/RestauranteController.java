package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDto;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoListDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteUpdateDto;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.service.RestauranteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public RestauranteDto buscar(@PathVariable Long id) {
//        o recomenaddo é fazer dentro da classe de servico, eu to fazendo aqui pq vou ter que
//        mudar muitas coisa em um projeto didatico, nao vale a pena
        Restaurante r = restauranteService.buscarOuFalhar(id);
        return new RestauranteDto(r);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteDto salvar(@RequestBody @Valid Restaurante restaurante) {
        Restaurante r = restauranteService.salvar(restaurante);
        return new RestauranteDto(r);
    }

    @PutMapping("/{id}")
    public RestauranteDto atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteUpdateDto data) {
        var a = new RestauranteDto(restauranteService.atualizar(id, new Restaurante(data)));
        return a;
    }

    //    o argumento HttpServletRequest request, é uma especificacao do jee, o spring injeta automaticamente
    @PatchMapping("/{id}")
    public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> restaurante, HttpServletRequest request) {
        return restauranteService.atualizarParcial(id, restaurante, request);
    }

    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable long id) {
        restauranteService.ativar(id);
    }

    @DeleteMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable long id) {
        restauranteService.desativar(id);
    }

    @GetMapping("/{id}/formaPagamento")
    public Set<FormaPagamentoListDto> listarFormasPagamento(@PathVariable Long id) {
        return restauranteService.listarFormasPagamento(id);
    }

    @DeleteMapping("/{idRestaurante}/formaPagamento/{idFormaPagamento}")
    public void desassociarFormaPagamento(@PathVariable Long idRestaurante, @PathVariable Long idFormaPagamento) {
        restauranteService.desassociarFormaPagamento(idRestaurante, idFormaPagamento);
    }

    @PutMapping("/{idRestaurante}/formaPagamento/{idFormaPagamento}")
    public void associarFormaPagamento(@PathVariable Long idRestaurante, @PathVariable Long idFormaPagamento) {
        restauranteService.associarFormaPagamento(idRestaurante, idFormaPagamento);
    }

    @PutMapping("/{idRestaurante}/abertura")
    public void abrirRestaurante(@PathVariable Long idRestaurante) {
        restauranteService.abrirRestaurante(idRestaurante);
    }

    @DeleteMapping("/{idRestaurante}/fechamento")
    public void fecharRestaurante(@PathVariable Long idRestaurante) {
        restauranteService.fecharRestaurante(idRestaurante);
    }


}

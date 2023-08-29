package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.api.openapi.controller.OpenApiRestauranteController;
import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoListDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteListDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteUpdateDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioListDto;
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
public class RestauranteController implements OpenApiRestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @Override
    @GetMapping
    public List<RestauranteListDto> listar() {
        return restauranteService.listar();
    }

    @Override
    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{id}")
    public RestauranteDto buscar(@PathVariable Long id) {
//        o recomenaddo é fazer dentro da classe de servico, eu to fazendo aqui pq vou ter que
//        mudar muitas coisa em um projeto didatico, nao vale a pena
        Restaurante r = restauranteService.buscarOuFalhar(id);
        return new RestauranteDto(r);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteDto salvar(@RequestBody @Valid Restaurante restaurante) {
        Restaurante r = restauranteService.salvar(restaurante);
        return new RestauranteDto(r);
    }

    @Override
    @PutMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public RestauranteDto atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteUpdateDto data) {
        var a = new RestauranteDto(restauranteService.atualizar(id, new Restaurante(data)));
        return a;
    }

    //    o argumento HttpServletRequest request, é uma especificacao do jee, o spring injeta automaticamente
    @Override
    @PatchMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> restaurante, HttpServletRequest request) {
        return restauranteService.atualizarParcial(id, restaurante, request);
    }

    @Override
    @PutMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable long id) {
        restauranteService.ativar(id);
    }

    @Override
    @DeleteMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable long id) {
        restauranteService.desativar(id);
    }

    @Override
    @PutMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@RequestBody List<Long> restaurantesIds) {
        restauranteService.ativarMultiplos(restaurantesIds);
    }

    @Override
    @DeleteMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@RequestBody List<Long> restaurantesIds) {
        restauranteService.desativarMultiplos(restaurantesIds);
    }

    @Override
    @GetMapping("/{id}/formaPagamento")
    @CheckSecurity.Restaurantes.PodeConsultar
    public List<FormaPagamentoListDto> listarFormasPagamento(@PathVariable Long id) {
        return restauranteService.listarFormasPagamento(id);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    public void desassociarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    public void associarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
    }

    @Override
    @PutMapping("/{restauranteId}/abertura")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrirRestaurante(@PathVariable Long restauranteId) {
        restauranteService.abrirRestaurante(restauranteId);
    }


    @Override
    @DeleteMapping("/{restauranteId}/fechamento")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fecharRestaurante(@PathVariable Long restauranteId) {
        restauranteService.fecharRestaurante(restauranteId);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @GetMapping("/{restauranteId}/responsaveis")
    public List<UsuarioListDto> listarUsuarios(@PathVariable Long restauranteId) {
        return restauranteService.listarUsuarios(restauranteId);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        restauranteService.associarUsuario(restauranteId, usuarioId);
    }

    @Override
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        restauranteService.desassociarUsuario(restauranteId, usuarioId);
    }


}

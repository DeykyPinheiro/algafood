package com.apigaworks.algafood.api.controller;

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
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping
    public List<RestauranteListDto> listar() {
        return restauranteService.listar();
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{id}")
    public RestauranteDto buscar(@PathVariable Long id) {
//        o recomenaddo é fazer dentro da classe de servico, eu to fazendo aqui pq vou ter que
//        mudar muitas coisa em um projeto didatico, nao vale a pena
        Restaurante r = restauranteService.buscarOuFalhar(id);
        return new RestauranteDto(r);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteDto salvar(@RequestBody @Valid Restaurante restaurante) {
        Restaurante r = restauranteService.salvar(restaurante);
        return new RestauranteDto(r);
    }

    @PutMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public RestauranteDto atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteUpdateDto data) {
        var a = new RestauranteDto(restauranteService.atualizar(id, new Restaurante(data)));
        return a;
    }

    //    o argumento HttpServletRequest request, é uma especificacao do jee, o spring injeta automaticamente
    @PatchMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> restaurante, HttpServletRequest request) {
        return restauranteService.atualizarParcial(id, restaurante, request);
    }

    @PutMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable long id) {
        restauranteService.ativar(id);
    }

    @DeleteMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable long id) {
        restauranteService.desativar(id);
    }

    @PutMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@RequestBody  List<Long> restaurantesIds) {
        restauranteService.ativarMultiplos(restaurantesIds);
    }

    @DeleteMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@RequestBody  List<Long> restaurantesIds) {
        restauranteService.desativarMultiplos(restaurantesIds);
    }

    @GetMapping("/{id}/formaPagamento")
    @CheckSecurity.Restaurantes.PodeConsultar
    public List<FormaPagamentoListDto> listarFormasPagamento(@PathVariable Long id) {
        return restauranteService.listarFormasPagamento(id);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    public void desassociarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    public void associarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
        restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
    }

    @PutMapping("/{restauranteId}/abertura")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void abrirRestaurante(@PathVariable Long restauranteId) {
        restauranteService.abrirRestaurante(restauranteId);
    }


    @DeleteMapping("/{restauranteId}/fechamento")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fecharRestaurante(@PathVariable Long restauranteId) {
        restauranteService.fecharRestaurante(restauranteId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @GetMapping("/{restauranteId}/responsaveis")
    public List<UsuarioListDto> listarUsuarios(@PathVariable Long restauranteId) {
        return restauranteService.listarUsuarios(restauranteId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        restauranteService.associarUsuario(restauranteId, usuarioId);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        restauranteService.desassociarUsuario(restauranteId, usuarioId);
    }


}

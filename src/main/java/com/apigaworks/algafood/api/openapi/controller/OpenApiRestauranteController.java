package com.apigaworks.algafood.api.openapi.controller;

import com.apigaworks.algafood.common.security.CheckSecurity;
import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoListDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteListDto;
import com.apigaworks.algafood.domain.dto.restaurante.RestauranteUpdateDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioListDto;
import com.apigaworks.algafood.domain.model.Restaurante;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurantes")
public interface OpenApiRestauranteController {

    @Operation(summary = "Lista restaurantes")
    @GetMapping
    List<RestauranteListDto> listar();

    @Operation(summary = "Busca restaurantes por Id")
    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{id}")
    RestauranteDto buscar(@PathVariable Long id);

    @Operation(summary = "Salva restaurantes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    RestauranteDto salvar(@RequestBody @Valid Restaurante restaurante);

    @Operation(summary = "Atualiza um restaurantes")
    @PutMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    RestauranteDto atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteUpdateDto data);

    //    o argumento HttpServletRequest request, é uma especificacao do jee, o spring injeta automaticamente
    @Operation(summary = "Atualiza parcialmente um restaurante")
    @PatchMapping("/{id}")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> restaurante, HttpServletRequest request);

    @Operation(summary = "Ativa um restaurantes")
    @PutMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void ativar(@PathVariable long id);

    @Operation(summary = "Desativa um restaurantes")
    @DeleteMapping("/{id}/ativar")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativar(@PathVariable long id);

    @Operation(summary = "Ativa todos os restaurantes por lista de Id")
    @PutMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void ativar(@RequestBody List<Long> restaurantesIds);

    @Operation(summary = "Desativa todos os restaurantes por lista de Id")
    @DeleteMapping("/ativacoes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativar(@RequestBody List<Long> restaurantesIds);

    @Operation(summary = "Lista forma de pagamento por restaurante")
    @GetMapping("/{id}/formaPagamento")
    @CheckSecurity.Restaurantes.PodeConsultar
    List<FormaPagamentoListDto> listarFormasPagamento(@PathVariable Long id);

    @Operation(summary = "Desassocia um restaurante de uma forma de pagamento")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    void desassociarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId);

    @Operation(summary = "Associa um restaurante a um forma de pagamento")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/formaPagamento/{formaPagamentoId}")
    void associarFormaPagamento(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId);

    @Operation(summary = "Abri um restaurante")
    @PutMapping("/{restauranteId}/abertura")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void abrirRestaurante(@PathVariable Long restauranteId);

    @Operation(summary = "Fecha um restaurante")
    @DeleteMapping("/{restauranteId}/fechamento")
    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void fecharRestaurante(@PathVariable Long restauranteId);

    @Operation(summary = "Lista responsaveis por restaurantes")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @GetMapping("/{restauranteId}/responsaveis")
    List<UsuarioListDto> listarUsuarios(@PathVariable Long restauranteId);

    @Operation(summary = "Associa um Usuario a um restaurante")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void associarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId);

    @Operation(summary = "Desassocia um Usuario de um restaurante")
    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{restauranteId}/responsaveis/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desassociarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId);
}

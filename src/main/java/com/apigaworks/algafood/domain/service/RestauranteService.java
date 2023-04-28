package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.el.util.ReflectionUtil;
import org.apache.ibatis.javassist.tools.reflect.Reflection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class RestauranteService {

    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO
            = "Não existe um cadastro de restaurante com código %d";

    private static final String MSG_RESTAURANTE_EM_USO
            = "restaurante de código %d não pode ser removido, pois está em uso";

    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaService cozinhaService;

    @Autowired
    public RestauranteService(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public Restaurante buscar(Long id) {
        return restauranteRepository.findById(id).get();
    }

    public Restaurante salvar(Restaurante restaurante) {
        cozinhaService.buscarOuFalhar(restaurante.getCozinha().getId());
        return restauranteRepository.save(restaurante);
    }

    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    public void remover(Long id) {
        try {
            Restaurante r = buscar(id);
            restauranteRepository.delete(r);
        } catch (EmptyResultDataAccessException e) {
            throw new RestauranteNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_RESTAURANTE_EM_USO, id));
        }

    }

    public Restaurante buscarOuFalhar(Long id) {
        return restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
    }

    public Restaurante atualizar(Long idRestaurante, Restaurante atualizacoesRestaurante) {
        Restaurante restauranteASerAtualizado = this.buscarOuFalhar(idRestaurante);
        BeanUtils.copyProperties(atualizacoesRestaurante, restauranteASerAtualizado, "id", "dataCadastro");
        return this.salvar(restauranteASerAtualizado);
    }


    public Restaurante atualizarParcial(Long id, Map<String, Object> dadosParcial, HttpServletRequest request) {
//        busquei o restaurante que vou atualizar
        Restaurante restaurante = buscarOuFalhar(id);

//        com a injetacao pelo spring de HttpServletRequest request, posso usar pra construir um
//        ServletServerHttpRequest que é o parametro construir um HttpMessageNotReadableException
        ServletServerHttpRequest serverHttpRequest  = new ServletServerHttpRequest(request);

        try {
//       cria o objeto que para mapear todos os dados, e mapeia para ele ser uma
//        objeto segundo paramentro do tipo colocado na convertValue
            ObjectMapper objectMapper = new ObjectMapper();

//        se a propriedade nao existe ou ela é ignorada, vai estourar uma exception,
//        mesma coisa que fiz no application.properties com as spring.jackson.deserialization
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

//        mapeias os dados
            Restaurante atualizacoes = objectMapper.convertValue(dadosParcial, Restaurante.class);

//        copiar as atualizacoes que chegaram, sem nulo nao intencional
//        dentro de um objeto que vai ser atualizado
            dadosParcial.forEach((nomePropriedade, valorPropriedade) -> {
//            pega o campo com nome equivalente do obejeto com as atualizacoes e quebra o encapsulamento
//            da classe, para poder alterar os dados de fora da classe
                Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                field.setAccessible(true);

//            pega o novo valor do objeto mapeado e coloca em uma variavel
//            peguei o field acima, pq string ele nao consegue pesquisar
                Object novoValor = ReflectionUtils.getField(field, atualizacoes);

//            pega o objeto a ser atualizado, passa o campo que precisa ser atualizado
//            e seta o novo valor dentro dele, como o laco for itera dentro dos dados recebidos
//            nao tem perigo de atualizar campos que nao foram enviados para null
                ReflectionUtils.setField(field, restaurante, novoValor);
            });
        } catch (IllegalArgumentException e){
//            tranformando IllegalArgumentException em  HttpMessageNotReadableException consigo tratar
//            da mesma forma que trato outras IllegalArgumentException que tenho na api
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
        }

//        salvar os novos valores
//        to usando o metodo salvar pq todas as mudancas ja foram refletidas,
//        logo nao preciso usar o metodo atualizar pq ja foram atualizadas
        return this.salvar(restaurante);
    }
}

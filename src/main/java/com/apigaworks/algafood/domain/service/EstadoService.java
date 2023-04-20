package com.apigaworks.algafood.domain.service;


import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    public static final String MSG_ESTADO_NAO_ENCONTRADO
            = "Não existe um cadastro de estado com código %d";

    private static final String MSG_ESTADO_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";

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
        try {
            Estado e = buscar(id);
            estadoRepository.delete(e);
        } catch (EmptyResultDataAccessException e) {
            throw new EstadoNaoEncontradoException(
                    String.format(MSG_ESTADO_NAO_ENCONTRADO, id));
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_ESTADO_EM_USO, id));
        }

    }

    public Estado buscarOuFalhar(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException(
                        String.format(MSG_ESTADO_NAO_ENCONTRADO, id)
                ));
    }

    public Estado atualizar(Long idEstado, Estado atualizacoesEstado) {
        Estado estadoAserAtualizado = buscarOuFalhar(idEstado);
        BeanUtils.copyProperties(atualizacoesEstado, estadoAserAtualizado, "id");
        return this.salvar(estadoAserAtualizado);
    }
}

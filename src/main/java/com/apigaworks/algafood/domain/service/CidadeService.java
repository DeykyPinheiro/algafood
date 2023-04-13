package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    public static final String MSG_CIDADE_NAO_ENCONTRADA
            = "Não existe um cadastro de cidade com código %d";

    private static final String MSG_CIDADE_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";

    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    public CidadeService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }


    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        Estado e = estadoService.buscarOuFalhar(estadoId);
        cidade.setEstado(e);
        return cidadeRepository.save(cidade);
    }

    public Cidade atualizar(Long id, Cidade cidadeNova) {
        Cidade cidadeAntiga = buscarOuFalhar(id);
        Long estadoId = cidadeNova.getEstado().getId();
        Estado e = estadoService.buscarOuFalhar(estadoId);
        cidadeNova.setEstado(e);
        BeanUtils.copyProperties(cidadeNova, cidadeAntiga, "id");
        return cidadeRepository.save(cidadeAntiga);
    }

    public Cidade buscarPorId(long id) {
        return cidadeRepository.findById(id).get();
    }

    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    public void remover(long id) {
        try {
            Cidade cidade = buscarPorId(id);
            cidadeRepository.delete(cidade);

        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(
                    String.format(MSG_CIDADE_NAO_ENCONTRADA, id));
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CIDADE_EM_USO, id));
        }
    }

    public Cidade buscarOuFalhar(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MSG_CIDADE_NAO_ENCONTRADA, id)
                ));
    }
}

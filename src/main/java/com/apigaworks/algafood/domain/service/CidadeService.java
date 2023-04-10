package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeService {

    public static final String MSG_CIDADE_NAO_ENCONTRADA
            = "Não existe um cadastro de cozinha com código %d";

    private static final String MSG_CIDADE_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";

    private CidadeRepository cidadeRepository;

    @Autowired
    public CidadeService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.save(cidade);
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


//
    }

    public Cidade buscarOuFalhar(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MSG_CIDADE_NAO_ENCONTRADA, id)
                ));
    }
}

package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CozinhaService {

    private static final String MSG_COZINHA_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";
    private CozinhaRepository cozinhaRepository;

    @Autowired
    public CozinhaService(CozinhaRepository cozinhaRepository) {
//        System.out.println("instanciei a porra do repository");
        this.cozinhaRepository = cozinhaRepository;
    }

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    public Cozinha buscarPorId(Long id) {
        return cozinhaRepository.findById(id).get();
    }

    @Transactional
    public void remover(Long id) {
        try {
            Cozinha c = cozinhaRepository.findById(id).get();
            cozinhaRepository.delete(c);

//            isso garante que o jpa vai descarregar todas as mundacas pendentes
//            no banco de dados, e aqui ele ja lanca a exception
            cozinhaRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new CozinhaNaoEncontradaException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COZINHA_EM_USO, id));
        }
    }

    public Cozinha atualizar(Long id, Cozinha cozinhaNova) {
//        TODO depois arrumar o BeanUtils.copyProperties pq alguns podem ser null
//        TODO e tbm usar um hashmap para receber e converter no objeto recebido
        Cozinha cozinhaAntiga = buscarPorId(id);
        BeanUtils.copyProperties(cozinhaNova, cozinhaAntiga, "id");
        return salvar(cozinhaAntiga);
    }

    public Cozinha buscarOuFalhar(Long id) {
        return cozinhaRepository.findById(id)
                .orElseThrow(() -> new CozinhaNaoEncontradaException(id));
    }

    public Cozinha buscarPrimeiro() {
        return cozinhaRepository.buscarPrimeiro().get();
    }

}

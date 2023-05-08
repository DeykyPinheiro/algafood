package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.formaPagamento.FormaPagamentoDTO;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.apigaworks.algafood.domain.model.FormaPagamento;
import com.apigaworks.algafood.domain.repository.FormaPagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FormaPagamentoService {

    private static final String MSG_PAGAMENTO_EM_USO
            = "Forma de pagamento de código %d não pode ser removido, pois está em uso";

    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    public FormaPagamentoService(FormaPagamentoRepository formaPagamentoRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;


    }
//     personRepository.findAll().stream().map(ListPersonDto::new).collect(Collectors.toList());

    public List<FormaPagamentoDTO> listar() {
        List<FormaPagamentoDTO> listaFormaPagamentos = formaPagamentoRepository.findAll()
                .stream().map(FormaPagamentoDTO::new).collect(Collectors.toList());
        return listaFormaPagamentos;
    }

    //    deveria retornar um FormaPagamentoDTO mas, é didatico
//    ja sei como fazer e nao quero perder tempo
    public FormaPagamentoDTO buscarOuFalhar(Long id) {
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new FormaPagamentoNaoEncontradaException(id));

        return new FormaPagamentoDTO(formaPagamento);
    }

    public FormaPagamentoDTO salvar(FormaPagamentoDTO formaPagamento) {
        formaPagamento = new FormaPagamentoDTO(formaPagamentoRepository.save(new FormaPagamento(formaPagamento)));
        return formaPagamento;
    }

    @Transactional
    public void excluir(long id) {
        try {
            FormaPagamento f = formaPagamentoRepository.findById(id).get();
            formaPagamentoRepository.delete(f);
            formaPagamentoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new FormaPagamentoNaoEncontradaException(id);
        } catch (NoSuchElementException e) {
            throw new FormaPagamentoNaoEncontradaException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PAGAMENTO_EM_USO, id));
        }


//        FormaPagamento formaPagamento = new FormaPagamento(this.buscarOuFalhar(id));
//        formaPagamentoRepository.delete(formaPagamento);
    }
}

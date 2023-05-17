package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.permissao.PermissaoDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoSaveDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoUpdateDto;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.GrupoNaoEncontratoException;
import com.apigaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.apigaworks.algafood.domain.model.Grupo;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissaoService {

    private static final String MSG_PERMISSAO_EM_USO
            = "Permissao de código %d não pode ser removido, pois está em uso";


    private PermissaoRepository permissaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional
    public PermissaoDto salvar(PermissaoSaveDto permissaoDto) {
        Permissao permissao = permissaoRepository.save(new Permissao(permissaoDto));
        return new PermissaoDto(permissao);
    }

    public PermissaoDto buscarOuFalhar(Long permissaoId) {
        Permissao permissao = permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
        return new PermissaoDto(permissao);
    }

    public List<PermissaoListDto> listar() {
        List<Permissao> listaPermissoes = permissaoRepository.findAll();
        return PermissaoListDto.converterLista(listaPermissoes);
    }


    @Transactional
    public PermissaoDto atualizar(Long permissaoId, PermissaoUpdateDto permissaoDto) {
        Permissao permissaoAtual = permissaoRepository.findById(buscarOuFalhar(permissaoId).id()).get();
        Permissao atualizacoes = new Permissao(permissaoDto);

        modelMapper.map(atualizacoes, permissaoAtual);
        return new PermissaoDto(permissaoAtual);
    }

    public void remover(Long permissaoId) {
        try {
            Permissao permissao = permissaoRepository.findById(buscarOuFalhar(permissaoId).id()).get();
            permissaoRepository.delete(permissao);
            permissaoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new PermissaoNaoEncontradaException(permissaoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PERMISSAO_EM_USO, permissaoId));
        }
    }

}

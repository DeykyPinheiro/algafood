package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.grupo.GrupoDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoListDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoSaveDto;
import com.apigaworks.algafood.domain.dto.grupo.GrupoUpdateDto;
import com.apigaworks.algafood.domain.dto.permissao.PermissaoListDto;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.GrupoNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Grupo;
import com.apigaworks.algafood.domain.model.Permissao;
import com.apigaworks.algafood.domain.repository.GrupoRepository;
import com.apigaworks.algafood.domain.repository.PermissaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    private static final String MSG_GRUPO_EM_USO
            = "Estado de código %d não pode ser removido, pois está em uso";

    private GrupoRepository grupoRepository;

//    @Autowired
//    private Permissa

    @Autowired
    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public GrupoDto salvar(GrupoSaveDto grupo) {
        Grupo g = grupoRepository.save(new Grupo(grupo));
        return new GrupoDto(g);
    }


    public List<GrupoListDto> listar() {
        List<Grupo> listaGrupos = grupoRepository.findAll();
//        return listaGrupos.stream().map(ListGrupoDto::new).collect(Collectors.toList());
        return GrupoListDto.converterLista(listaGrupos);
    }

    public GrupoDto buscarOuFalhar(Long id) {
        Grupo grupo = grupoRepository.findById(id)
                .orElseThrow(() -> new GrupoNaoEncontratoException(id));
        return new GrupoDto(grupo);
    }

    @Transactional
    public GrupoDto atualizar(Long id, GrupoUpdateDto grupo) {
        Grupo grupoAntigo = new Grupo(this.buscarOuFalhar(id));
        BeanUtils.copyProperties(grupo, grupoAntigo, "id");
        return new GrupoDto(grupoAntigo);
    }


    @Transactional
    public void remover(Long id) {
        try {
            Grupo g = new Grupo(this.buscarOuFalhar(id));
            grupoRepository.delete(g);

            grupoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontratoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, id));
        }
    }

    public List<PermissaoListDto> listaPermissoesPorGrupo(Long grupoId) {
        Grupo grupo = grupoRepository.findById(buscarOuFalhar(grupoId).id()).get();
        return PermissaoListDto.converterLista(grupo.getListaPermissao());
    }

//    @Transactional
//    public void associarPermissao(Long grupoId, Long permissaoId) {
//        Grupo grupo = grupoRepository.findById(buscarOuFalhar(grupoId).id()).get();
//        Permissao permissao =
//    }

}

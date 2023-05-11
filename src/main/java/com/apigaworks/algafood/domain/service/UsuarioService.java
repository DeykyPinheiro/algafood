package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.usuario.UsuarioDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioListDto;
import com.apigaworks.algafood.domain.dto.usuario.UsuarioSaveDto;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.GrupoNaoEncontratoException;
import com.apigaworks.algafood.domain.exception.UsuarioNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final String MSG_USUARIO_EM_USO
            = "Usuario de código %d não pode ser removido, pois está em uso";

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDto buscarOuFalhar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontratoException(id));
        return new UsuarioDto(usuario);
    }


    @Transactional
    public UsuarioDto salvar(UsuarioSaveDto usuarioDto) {
        Usuario usuario = usuarioRepository.save(new Usuario(usuarioDto));
        return new UsuarioDto(usuario);
    }

    public List<UsuarioListDto> listar() {
        List<Usuario> listaUsuario = usuarioRepository.findAll();
        return UsuarioListDto.converterLista(listaUsuario);
    }

    @Transactional
    public void remover(Long id) {
        try {
            Usuario usuario = new Usuario(buscarOuFalhar(id));
            usuarioRepository.delete(usuario);
//                    usuarioRepository.delete(usuarioRepository.findById(id).get());

//        isso garante que o jpa vai descarregar todas as mundacas pendentes
//            no banco de dados, e aqui ele ja lanca a exception, isso evita
//            que a exception saia do try
            usuarioRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new UsuarioNaoEncontratoException(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_USUARIO_EM_USO, id));
        }
    }
}

package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.dto.usuario.*;
import com.apigaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.apigaworks.algafood.domain.exception.GrupoNaoEncontratoException;
import com.apigaworks.algafood.domain.exception.NegocioException;
import com.apigaworks.algafood.domain.exception.UsuarioNaoEncontratoException;
import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private ModelMapper modelMapper;

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

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDto.email());
        if (usuarioExistente.isPresent()){
            throw new NegocioException("Já existe um usuario com o e-mail informado");
        }
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

    @Transactional
    public UsuarioDto atualizar(Long id, UsuarioUpdateDto usuario) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.email());



//        cuidado nessas conversoes, como são representativos, podemos
//        perder dados como data e tudo mais
        UsuarioDto usuarioDto = buscarOuFalhar(id);
        Usuario usuarioAtual = usuarioRepository.findById(usuarioDto.id()).get();
        Usuario atualizacoes = new Usuario(usuario);


//        isso verifica se o email existe, caso exista o usuario que eu tenho no banco e o usuario que eu estou
//        mandando tesm que ser diferentes
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuarioAtual)){
            throw new NegocioException("Já existe um usuario com o e-mail informado");
        }


//        nao seja burro krai, pelo amor de deus, obrigado modelMapper
        modelMapper.map(atualizacoes, usuarioAtual);
        return new UsuarioDto(usuarioAtual);
    }

    @Transactional
    public void atualizarSenha(Long id, UsuarioUpdateSenhaDto usuario) {


        UsuarioDto usuarioDto = buscarOuFalhar(id);
        Usuario usuarioAtual = usuarioRepository.findById(usuarioDto.id()).get();
        Usuario atualizacoes = new Usuario(usuario);


        if (usuarioAtual.getSenha().equals(usuario.senhaAtual())) {
            modelMapper.map(atualizacoes, usuarioAtual);
        } else {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário");
        }
    }
}

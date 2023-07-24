package com.apigaworks.algafood.common.security.authorizationserver;


import com.apigaworks.algafood.domain.model.Usuario;
import com.apigaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

//essa classe é responsavel por buscar os usuarios em banco de dados, sem ela ele busca na memoria
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true) // tive que adicionar pq a transacao fecha quando consulta e-mail
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuario não encontrado com esse e-mail!"));
//estou passando uma lista vazia por hora
        System.out.println("CHAMEI FINALMENTE loadUserByUsername");
        return new User(user.getEmail(), user.getSenha(),getAuthorities(user).stream().toList());
//        return new User(user.getEmail(), user.getSenha(), Collections.emptyList());
    }

    private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
        return usuario.getListaGrupos().stream()
                .flatMap(grupo -> grupo.getListaPermissao().stream())
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
//http://localhost:9000/oauth2/authorize?client_id=client&redirect_uri=https%3A%2F%2Foauthdebugger.com%2Fdebug&scope=READ&response_type=code&response_mode=form_post&state=cce93xkg8tu&nonce=t0he77hnqgp

package com.apigaworks.algafood.common.security.authorizationserver;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//TODO
//talvez isso seja o DTO que eu devolvo
public record CustomPasswordUser(String username, Collection<GrantedAuthority> authorities) {

}

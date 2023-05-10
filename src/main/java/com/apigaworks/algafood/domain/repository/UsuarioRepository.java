package com.apigaworks.algafood.domain.repository;

import com.apigaworks.algafood.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

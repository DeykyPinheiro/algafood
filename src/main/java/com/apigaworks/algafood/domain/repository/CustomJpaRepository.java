package com.apigaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

// quando for extender o jpa ou qualquer interface nao esqueca de ativar no "main"
// que no caso é assim:
// @EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> buscarPrimeiro();

}
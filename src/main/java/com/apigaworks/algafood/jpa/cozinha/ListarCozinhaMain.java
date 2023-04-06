package com.apigaworks.algafood.jpa.cozinha;


import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ListarCozinhaMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
        List<Cozinha> cozinhaList = cozinhaRepository.findAll();
        for (Cozinha cozinha : cozinhaList) {
            System.out.printf("%s - %s\n", cozinha.getId(), cozinha.getNome());
        }
    }
}

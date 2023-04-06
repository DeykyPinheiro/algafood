package com.apigaworks.algafood.jpa.cozinha;

import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ExclusaoCozinhaMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);
        Cozinha c = new Cozinha();
        c.setId(1L);

        cozinhaRepository.delete(c);
    }
}

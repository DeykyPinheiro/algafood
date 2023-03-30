package com.apigaworks.algafood.jpa.cozinha;

import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InclusaoCozinhaMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);

        Cozinha c1 = new Cozinha("Japonesa");
        Cozinha c2 = new Cozinha("Brasileira");

        c1 = cozinhaRepository.salvar(c1);
        c2 = cozinhaRepository.salvar(c2);

        System.out.printf("%d - %s\n", c1.getId(), c1.getNome());
        System.out.printf("%d - %s\n", c2.getId(), c2.getNome());

    }
}

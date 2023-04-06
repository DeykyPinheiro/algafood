package com.apigaworks.algafood.jpa.cidade;

import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class InclusaoCidadeMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CidadeRepository cidadeRepository = applicationContext.getBean(CidadeRepository.class);
        Cidade c1 = new Cidade();
        c1.setNome("c1");

        Cidade c2 = new Cidade();
        c2.setNome("c2");

        c1 = cidadeRepository.save(c1);
        c2 = cidadeRepository.save(c2);

        System.out.printf("%s - %s\n", c1.getId(), c1.getNome());
        System.out.printf("%s - %s\n", c2.getId(), c2.getNome());
    }
}

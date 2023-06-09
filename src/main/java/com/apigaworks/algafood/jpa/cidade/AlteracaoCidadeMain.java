package com.apigaworks.algafood.jpa.cidade;


import com.apigaworks.algafood.AlgafoodApplication;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class AlteracaoCidadeMain {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CidadeRepository cidadeRepository = applicationContext.getBean(CidadeRepository.class);
        List<Cidade> cidadeList = cidadeRepository.findAll();
        for (Cidade c : cidadeList) {
            System.out.printf("%s - %s\n", c.getId(), c.getNome());
        }

    }
}

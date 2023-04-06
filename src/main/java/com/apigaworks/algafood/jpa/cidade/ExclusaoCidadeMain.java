package com.apigaworks.algafood.jpa.cidade;

import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class ExclusaoCidadeMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        CidadeRepository cidadeRepository = applicationContext.getBean(CidadeRepository.class);
        Cidade c  = new Cidade();
        c.setId(1L);
        cidadeRepository.delete(c);
    }
}

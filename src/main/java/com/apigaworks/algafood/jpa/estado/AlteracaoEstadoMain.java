package com.apigaworks.algafood.jpa.estado;

import com.apigaworks.algafood.AlgafoodApplication;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class AlteracaoEstadoMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        EstadoRepository estadoRepository = applicationContext.getBean(EstadoRepository.class);
        Estado e = new Estado();
        e.setId(1L);
        e.setNome("nome teste");

        e = estadoRepository.save(e);
        System.out.printf("%s - %s\n", e.getId(), e.getNome());
    }
}

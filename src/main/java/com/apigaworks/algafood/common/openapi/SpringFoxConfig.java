package com.apigaworks.algafood.common.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SpringFoxConfig {

    //    Docket é sumario em ptbr
    @Bean
    public Docket apiDocket() {
//        o DocumentationType é o tipo de json que vai gerar
//        select devolver um builder
//        apis() é para mapear os metodos que vao ser listados na documentacao
//
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();
    }
}
package com.apigaworks.algafood;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.infrastructure.CustomJpaRepositoryImpl;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class AlgafoodApplication {



    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        var app = new SpringApplication(AlgafoodApplication.class);
//        app.addListeners(new Base64ProtocolResolver()); // adivionando o listner que eu registrei, que é como tinha feito antes
        app.addInitializers(new Base64ProtocolResolver()); // adivionando o listner que eu registrei
        app.run(args);


//        SpringApplication.run(AlgafoodApplication.class, args);
    }

}

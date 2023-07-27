package com.apigaworks.algafood.common.io;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.Base64;

//SPI, service provider interface, serve pra extensoes/ customizacoes
public class Base64ProtocolResolver implements ProtocolResolver,
        ApplicationListener<ApplicationContextInitializedEvent> {

    //    location é toda a string
//    isso aqui ta pronto e certo, mas preciso registrar, e temos que fazer isso na mais do spring
//    temos que por tbm um listener
    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (location.startsWith("base64:")) {
            byte[] decodeResource = Base64.getDecoder().decode(location.substring(7));
            return new ByteArrayResource(decodeResource);
        }
        return null;
    }

//    quando iniciar, o contexto, vai ser iniciada,
//    mas ainda tem que mostrar pro spring
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        event.getApplicationContext().addProtocolResolver(this);

    }
}

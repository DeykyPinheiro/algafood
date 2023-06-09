package com.apigaworks.algafood.common.jackson;

import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.model.mixin.RestauranteMixin;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

//usamos essa classe pra registrar serealizadores das classes
@Component
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule() {
//        primeiro a classe que é pra ter as validacoes, e depois da onde ele tira as anotacoes jackson
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
    }
}

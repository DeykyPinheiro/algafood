package com.apigaworks.algafood.core.modelmapper;

import com.apigaworks.algafood.domain.model.Usuario;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    //    estou usando isso para instanciar uma lib externa, como aprendi
//    no comeco do curso, eu pre configurei para nao copiar os nulos
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

//     modelMapper.getConfiguration()
//             .setSkipNullEnabled(true)
//        .setAmbiguityIgnored(true)
//        .setMatchingStrategy(MatchingStrategies.STRICT)
//        .setPropertyCondition(Conditions.isNotNull());

}
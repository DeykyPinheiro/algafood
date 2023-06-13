package com.apigaworks.algafood.common.email;

import com.apigaworks.algafood.domain.service.EnvioEmailService;
import com.apigaworks.algafood.infrastructure.service.email.SmtpEnvioEmailService;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


//algafood.email isso serve pra configurar/ reconhecer tudo que vem com esse prefixo

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

//    feito automatico o bind das propriedades ques estaou com esse prefixo, e o resto dos nomes
    @NotNull
    private String remetente;

    //    puxa a implementacao do applicaction.properties, se nao tiver ele deixa com defautl(FAKE)
    //    ja retorno a instancia da funcao de email que sera usada
    private Implementacao impl = Implementacao.FAKE;

//    criei dentro de uma classe, pq ficar entendivel, pelo apricaion properfeites
    private Sandbox sandbox = new Sandbox();

}

package com.apigaworks.algafood.common.openapi;

import com.apigaworks.algafood.api.exceptionhandler.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.validation.Schema;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                scopes = {
                        @OAuthScope(name = "READ")
                }
        )))
public class OpenApiConfig {

    List<Tag> tags = Arrays.asList(
            new Tag().name("Cidades").description("Gerencia cidades"),
            new Tag().name("Usuarios").description("Gerencia  Usuarios"),
            new Tag().name("Fotos").description("Gerencia  Fotos"),
            new Tag().name("Restaurantes").description("Gerencia Restaurantes"),
            new Tag().name("Produtos").description("Gerencia Produtos"),
            new Tag().name("Permissao").description("Gerencia Permissao"),
            new Tag().name("Pedidos").description("Gerencia Pedidos"),
            new Tag().name("Grupos").description("Gerencia Grupos"),
            new Tag().name("Estados").description("Gerencia Estados"),
            new Tag().name("Forma De Pagamento").description("Gerencia Forma De Pagamento"),
            new Tag().name("Estatisticas").description("Gera relatorios"),
            new Tag().name("Cozinhas").description("Gerencia Cozinhas"),
            new Tag().name("CheckHost").description("Mostra host Atual")
    );

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Algafood - API")
                        .version("v1")
                        .description("Rest API do Algafood")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                
                                
                        .tags(tags);
                        
    }


}
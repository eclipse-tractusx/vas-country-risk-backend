package com.catenax.valueaddedservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().addServersItem(new Server().url("/"))
                .addSecurityItem(new SecurityRequirement().addList("open_id_scheme", List.of("read", "write")))
                .info(new Info().title("VAS API")
                        .version("1.0")
                        .description("Swagger documentation for the Value Added Services APIs"));
    }

    @Bean
    public OpenApiCustomiser customiser() {
        return openApi -> {
            final Components components = openApi.getComponents();
            components.addSecuritySchemes("open_id_scheme", new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                    .flows(new OAuthFlows().authorizationCode(
                            new OAuthFlow().authorizationUrl("https://centralidp.dev.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/auth")
                                    .tokenUrl("https://centralidp.dev.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/token"))));
        };
    }

}

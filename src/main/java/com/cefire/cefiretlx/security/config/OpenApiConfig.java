package com.cefire.cefiretlx.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String SCHEME_NAME = "bearerAuth";
  private static final String BEARER_FORMAT = "JWT";
  private static final String DESCRIPTION = "JWT Authentication para la API de Blogs";

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        .components(new Components()
            .addSecuritySchemes(SCHEME_NAME,
                  new SecurityScheme()
                      .name(SCHEME_NAME)
                      .type(SecurityScheme.Type.HTTP)
                      .scheme("bearer")
                      .bearerFormat(BEARER_FORMAT)
                      .in(SecurityScheme.In.HEADER)
                      .description(DESCRIPTION)
                )
        )
        .info(new Info()
            .title("Gestión de blogs API")
            .version("1.0")
            .description("API Restfull para la gestion de blogs de la clinica CEFIRETLX")
            .contact(new Contact()
                .name("Erick Cruz")
                .email("erick.doev@gmail.com")
                .url("https://www.cefiretlx.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
            )
        );
  };
}

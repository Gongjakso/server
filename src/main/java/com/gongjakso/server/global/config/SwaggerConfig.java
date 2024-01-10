package com.gongjakso.server.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("Gongjakso Server Swagger UI")
                .description("공작소 서버 API 테스트 페이지입니다.")
                .version("v0.0.1");

//        String jwtSchemeName = TokenProvider.AUTHORIZATION_HEADER;
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//                        .name(jwtSchemeName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme(BEARER_TOKEN_PREFIX)
//                        .bearerFormat(TokenProvider.TYPE));

        // Swagger UI 접속 후, 딱 한 번만 accessToken을 입력해주면 모든 API에 토큰 인증 작업이 적용됩니다.
        return new OpenAPI()
                .info(info)
                //.addSecurityItem(securityRequirement)
                .components(new Components());
    }
}
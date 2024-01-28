package com.gongjakso.server.global.config;

import com.gongjakso.server.global.security.jwt.JwtAccessDeniedHandler;
import com.gongjakso.server.global.security.jwt.JwtAuthenticationEntryPoint;
import com.gongjakso.server.global.security.jwt.JwtFilter;
import com.gongjakso.server.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * FilterChain 설정
     * @param http - 시큐리티 설정을 담당하는 객체
     * @return - FilterChain 진행 값 반환
     * @throws Exception - 시큐리티 설정 관련 모든 예외 (JWT 관련 포함)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 허용, CSRF 비활성화
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);

        // Session 미사용
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // httpBasic, httpFormLogin 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        // JWT 관련 필터 설정 및 예외 처리
        http.exceptionHandling((exceptionHandling) ->
            exceptionHandling
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        // 요청 URI별 권한 설정
        http.authorizeHttpRequests((authorize) ->
                // Swagger UI 외부 접속 허용
                authorize.requestMatchers( "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 로그인 로직 접속 허용
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // 메인 페이지, 공고 페이지 등에 한해 인증 정보 없이 접근 가능 (추후 추가)
                        // 이외의 모든 요청은 인증 정보 필요
                        .anyRequest().permitAll());

        // JWT 관련 환경 설정


        return http.build();
    }

    /**
     * CORS 허용하도록 커스터마이징 진행
     * @return - 변경된 CORS 정책 정보 반환
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 인증정보 주고받도록 허용
        config.setAllowCredentials(true);
        // 허용할 주소
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        // 허용할 HTTP Method
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // 허용할 헤더 정보
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }
}

package org.example.contenedor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                // CLIENTE puede registrar contenedor
                .requestMatchers(HttpMethod.POST, "/contenedores/**").hasRole("CLIENTE")

                // OPERADOR/ADMIN puede modificar
                .requestMatchers(HttpMethod.PUT, "/contenedores/**").hasAnyRole("OPERADOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/contenedores/**").hasAnyRole("OPERADOR", "ADMIN")

                // CLIENTE, OPERADOR y ADMIN pueden consultar
                .requestMatchers(HttpMethod.GET, "/contenedores/**")
                    .hasAnyRole("CLIENTE", "OPERADOR", "ADMIN")

                .anyRequest().authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null || realmAccess.get("roles") == null)
                return List.of();

            return ((List<String>) realmAccess.get("roles")).stream()
                    .map(r -> "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}

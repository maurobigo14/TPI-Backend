package org.example.solicitud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.http.HttpMethod;


import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                // CLIENTE crea solicitudes
                .requestMatchers(HttpMethod.POST, "/solicitudes/**").hasRole("CLIENTE")

                // OPERADOR programa o cambia estado
                .requestMatchers("/solicitudes/programar/**").hasRole("OPERADOR")
                .requestMatchers("/solicitudes/estado/**").hasRole("OPERADOR")

                // CLIENTE, OPERADOR, ADMIN pueden consultar
                .requestMatchers(HttpMethod.GET, "/solicitudes/**")
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
            Collection<GrantedAuthority> authorities = new java.util.ArrayList<>();
            
            // Obtener roles de realm_access
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.get("roles") != null) {
                List<String> realmRoles = (List<String>) realmAccess.get("roles");
                realmRoles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .forEach(authorities::add);
            }
            
            // Obtener roles de resource_access (para cualquier servicio)
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null) {
                resourceAccess.values().stream()
                    .filter(resource -> resource instanceof Map)
                    .map(resource -> (Map<String, Object>) resource)
                    .filter(resource -> resource.containsKey("roles"))
                    .flatMap(resource -> ((List<String>) resource.get("roles")).stream())
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .forEach(authorities::add);
            }
            
            return authorities;
        });

        return converter;
    }
}


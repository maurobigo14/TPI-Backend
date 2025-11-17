package org.example.solicitud.config;

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
                .requestMatchers(
                        "/actuator/health",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // CLIENTE registra solicitud (pedido de traslado)
                .requestMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")

                // CLIENTE consulta estado de su contenedor y solicitudes
                .requestMatchers(HttpMethod.GET, "/solicitudes/**")
                    .hasAnyRole("CLIENTE", "OPERADOR")

                // OPERADOR genera rutas tentativas (costo y tiempo estimado)
                .requestMatchers(HttpMethod.POST, "/solicitudes/rutas/tentativas").hasRole("OPERADOR")

                // OPERADOR asigna rutas y camiones a tramos
                .requestMatchers(HttpMethod.POST, "/solicitudes/rutas/asignar").hasRole("OPERADOR")
                .requestMatchers(HttpMethod.POST, "/solicitudes/tramos/asignar-camion").hasRole("OPERADOR")

                // TRANSPORTISTA registra inicio/fin de tramo y ve sus tramos asignados
                .requestMatchers(HttpMethod.POST, "/solicitudes/tramos/*/iniciar").hasRole("TRANSPORTISTA")
                .requestMatchers(HttpMethod.POST, "/solicitudes/tramos/*/finalizar").hasRole("TRANSPORTISTA")
                .requestMatchers(HttpMethod.GET, "/solicitudes/transportistas/*/tramos").hasRole("TRANSPORTISTA")

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

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(r -> "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return converter;
    }
}


package org.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder
            .withJwkSetUri("http://keycloak:8080/realms/tpi/protocol/openid-connect/certs")
            .jwsAlgorithm(SignatureAlgorithm.RS256)
            .build();
        
        // No validar issuer - solo firma y expiración
        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("http://localhost:8088/realms/tpi"));
        
        return jwtDecoder;
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

        http
            // El gateway NO necesita CSRF
            .csrf(ServerHttpSecurity.CsrfSpec::disable)

            .authorizeExchange(exchange -> exchange
                // 🔓 Endpoints públicos
                .pathMatchers(
                    "/actuator/health",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // 🔒 TODO lo demás requiere token válido
                .anyExchange().authenticated()
            )

            // Habilitar JWT como resource server
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(reactiveJwtAuthConverter())
                       .jwtDecoder(jwtDecoder())
                )
            );

        return http.build();
    }

    // Convertir los roles de Keycloak a roles de Spring ("ROLE_...")
    @SuppressWarnings("unchecked")
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null || realmAccess.get("roles") == null) {
                return List.of();
            }

            List<String> roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                .map(role -> "ROLE_" + role) // CLIENTE -> ROLE_CLIENTE
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        });

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}

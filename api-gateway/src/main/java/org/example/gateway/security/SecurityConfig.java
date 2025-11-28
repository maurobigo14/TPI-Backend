package org.example.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers(
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                // Solicitudes: crear y consultar (Cliente/Operador/Admin)
                .pathMatchers(HttpMethod.POST, "/api/solicitudes").hasAnyRole("CLIENTE", "OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.GET, "/api/solicitudes/**").hasAnyRole("CLIENTE", "OPERADOR", "ADMIN")
                // Rutas tentativas y asignación de ruta (Operador/Admin)
                .pathMatchers(HttpMethod.POST, "/api/solicitudes/rutas/**").hasAnyRole("OPERADOR", "ADMIN")
                // Asignación de camión (Operador/Admin)
                .pathMatchers(HttpMethod.POST, "/api/solicitudes/tramos/asignar-camion").hasAnyRole("OPERADOR", "ADMIN")
                // Iniciar/Finalizar tramo (Transportista/Admin)
                .pathMatchers(HttpMethod.POST, "/api/solicitudes/tramos/*/iniciar").hasAnyRole("TRANSPORTISTA", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/api/solicitudes/tramos/*/finalizar").hasAnyRole("TRANSPORTISTA", "ADMIN")
                // Contenedores consultas (Operador/Admin)
                .pathMatchers(HttpMethod.GET, "/api/contenedores/**").hasAnyRole("OPERADOR", "ADMIN")
                // Camiones CRUD (Operador/Admin) y consultas (Operador/Transportista/Admin)
                .pathMatchers(HttpMethod.GET, "/api/camiones/**").hasAnyRole("OPERADOR", "TRANSPORTISTA", "ADMIN")
                .pathMatchers(HttpMethod.POST, "/api/camiones/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/camiones/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/camiones/**").hasAnyRole("OPERADOR", "ADMIN")
                // Depósitos CRUD (Operador/Admin)
                .pathMatchers(HttpMethod.POST, "/api/depositos/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/depositos/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/depositos/**").hasAnyRole("OPERADOR", "ADMIN")
                // Tarifas CRUD (Operador/Admin) y consulta autenticada
                .pathMatchers(HttpMethod.POST, "/api/tarifas/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/tarifas/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/tarifas/**").hasAnyRole("OPERADOR", "ADMIN")
                .pathMatchers(HttpMethod.GET, "/api/tarifas/**").authenticated()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        return new KeycloakRealmRoleConverter().convert(jwt);
    }
}

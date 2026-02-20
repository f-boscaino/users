package com.intesigroup.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
    private String clientId;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(
                new KeycloakJwtRolesConverter(clientId)
        );

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/openapi/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/**").hasRole("read_user")
                    .requestMatchers(HttpMethod.POST, "/").hasRole("create_user")
                    .requestMatchers(HttpMethod.PUT, "/").hasRole("update_user")
                    .requestMatchers(HttpMethod.DELETE, "/").hasRole("delete_user")
            ).oauth2ResourceServer(
                    oauth2 ->
                            oauth2.jwt(jwt ->
                                    jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }

    private class KeycloakJwtRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
        private static final String ROLES_PROPERTY = "roles";
        private final String clientId;

        public KeycloakJwtRolesConverter(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public Collection<GrantedAuthority> convert(Jwt source) {
            Set<String> roles = new HashSet<>();

            Map<String, Object> resourceAccess = source.getClaim(RESOURCE_ACCESS_CLAIM);
            if (resourceAccess != null && clientId != null) {
                Object client = resourceAccess.get(clientId);
                if (client instanceof Map<?, ?> clientMap) {
                    Object clientRoles = clientMap.get(ROLES_PROPERTY);
                    if (clientRoles instanceof Collection<?> cr) {
                        cr.forEach(r -> roles.add(String.valueOf(r)));
                    }
                }
            }
            return roles.stream()
                    .filter(r -> r != null && !r.isBlank())
                    .map(r -> "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }
}
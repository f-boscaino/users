package com.intesigroup.users.service;

import com.intesigroup.users.entity.LoggedUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    public List<String> parseRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return List.of();

        Object roles = realmAccess.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    public LoggedUser getUser(Jwt jwt) {
        String usernameClaim = jwt.getClaim("preferred_username");
        String emailClaim = jwt.getClaim("email");
        return LoggedUser.builder()
                .username(usernameClaim)
                .email(emailClaim)
                .loggedAt(LocalDateTime.now())
                .build();
    }
}

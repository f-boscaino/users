package com.intesigroup.users.service;

import com.intesigroup.users.entity.LoggedUser;
import com.intesigroup.users.enums.Operation;
import com.intesigroup.users.repository.LoggedUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogService {
    private AuthService authService;
    private LoggedUserRepository loggedUserRepository;

    public void log(Jwt jwt, Operation operation) {
        LoggedUser loggedUser = authService.getUser(jwt);
        loggedUser.setOperation(operation.name());
        loggedUserRepository.save(loggedUser);
    }
}

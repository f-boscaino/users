package com.intesigroup.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.enums.Operation;
import com.intesigroup.users.service.LogService;
import com.intesigroup.users.service.UserWithAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController("/user")
@AllArgsConstructor
public class UserController {

    private UserWithAuthService userWithAuthService;
    private LogService logService;

    @PostMapping
    public User addUser(@RequestBody @Valid User user, @AuthenticationPrincipal Jwt jwt) {
        logService.log(jwt, Operation.ADD_USER);
        return userWithAuthService.addUser(user);
    }

    @GetMapping("/all")
    public String getAllUsers(@AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        logService.log(jwt, Operation.READ_USERS);
        return userWithAuthService.getAllUsers(jwt);
    }


    @GetMapping
    public String getUserByEmail(@RequestParam("email") String email, @AuthenticationPrincipal Jwt jwt) throws JsonProcessingException {
        logService.log(jwt, Operation.READ_USER);
        return userWithAuthService.getUserByEmail(email, jwt);
    }

    @PutMapping
    public User updateUser(@RequestParam("email") String email, @RequestBody @Valid User user, @AuthenticationPrincipal Jwt jwt) {
        logService.log(jwt, Operation.UPDATE_USER);
        return userWithAuthService.updateUser(email, user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam("email") String email, @AuthenticationPrincipal Jwt jwt) {
        logService.log(jwt, Operation.DELETE_USER);
        userWithAuthService.deleteUserByEmail(email);
    }
}

package com.intesigroup.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.mapper.UserToUserResponseMapper;
import com.intesigroup.users.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UserWithAuthService {
    UserService userService;
    AuthService authService;

    final Map<String, Function<User, UserResponse>> mappers = Map.of(
            "OPERATOR", UserToUserResponseMapper.MAPPER::userToOperatorResponse,
            "USER", UserToUserResponseMapper.MAPPER::userToUserResponse,
            "ADMIN",UserToUserResponseMapper.MAPPER::userToAdminResponse);

    public User addUser(User user) {
        return userService.addUser(user);
    }


    public String getAllUsers(Jwt jwt) throws JsonProcessingException {
        List<User> userList = userService.getAllUsers();
        List<String> roles = authService.parseRoles(jwt);

        return parseUserListByRole(getHigherRole(roles), userList);
    }

    public String getUserByEmail(String email, Jwt jwt) throws JsonProcessingException {
        User user = userService.getUserByMail(email);
        List<String> roles = authService.parseRoles(jwt);

        return parseUserByRole(getHigherRole(roles), user);
    }

    public User updateUser(String email, User user) {
        return userService.updateUser(email, user);
    }

    public void deleteUserByEmail(String email) {
        userService.deleteUserByEmail(email);
    }

    private String parseUserListByRole(String role, List<User> userList) throws JsonProcessingException {
        if(role != null && mappers.containsKey(role)) {
            return new ObjectMapper().writeValueAsString(userList.stream().map(mappers.get(role)).toList());
        } else {
            return null;
        }
    }

    private String parseUserByRole(String role, User user) throws JsonProcessingException {
        if(role != null && mappers.containsKey(role)) {
            return new ObjectMapper().writeValueAsString(mappers.get(role).apply(user));
        } else {
            return null;
        }
    }

    private String getHigherRole( List<String> roles){
        if(roles.contains("ADMIN")){
            return "ADMIN";
        }
        if(roles.contains("OPERATOR")){
            return "OPERATOR";
        }
        if(roles.contains("USER")){
            return "USER";
        }
        return null;
    }
}

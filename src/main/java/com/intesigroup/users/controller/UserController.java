package com.intesigroup.users.controller;

import com.intesigroup.users.entity.User;
import com.intesigroup.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController("/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping
    public User getUserByEmail(@RequestParam("email") String email) {
        return userService.getUserByMail(email);
    }

    @PutMapping
    public User updateUser(@RequestParam("email") String email, @RequestBody User user) {
        return userService.updateUser(email, user);
    }

    @DeleteMapping
    public void updateUser(@RequestParam("email") String email) {
        userService.deleteUserByEmail(email);
    }

}

package com.intesigroup.users.service;

import com.intesigroup.users.entity.Role;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByMail(String email) {
        return userRepository.findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String email, User user) {
        User existingUser = getUserByMail(email);
        existingUser.update(user);
        return userRepository.save(existingUser);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteById(email);
    }

    @Transactional
    public List<Role> getUserRoles(String email) {
        User user = getUserByMail(email);
        return new ArrayList<>(user.getRoleList());
    }

}

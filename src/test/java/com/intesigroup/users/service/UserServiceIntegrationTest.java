package com.intesigroup.users.service;

import com.intesigroup.users.entity.Role;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.enums.RoleType;
import com.intesigroup.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    UserService underTest;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void addUserShouldSuccessfullyCreateTheUser() {
        List<Role> roleList = List.of(Role.builder().roleType(RoleType.OWNER).build(), Role.builder().roleType(RoleType.OPERATOR).build());
        String email = "email@email.com";
        User user = User.builder().email(email).roleList(roleList).build();
        underTest.addUser(user);

        Optional<User> retrievedUser = userRepository.findById(email);

        assertTrue(retrievedUser.isPresent());
        assertEquals(email, retrievedUser.get().getEmail());

        List<Role> roles = underTest.getUserRoles(email);

        assertTrue(roles.stream().anyMatch(el -> el.getRoleType().equals(RoleType.OWNER)));
        assertTrue(roles.stream().anyMatch(el -> el.getRoleType().equals(RoleType.OPERATOR)));
        assertFalse(roles.stream().anyMatch(el -> el.getRoleType().equals(RoleType.DEVELOPER)));
    }

    @Test
    public void getUserByMailShouldReturnAnExceptionIfNotExisting() {
        assertThrows(ResponseStatusException.class,
                ()-> underTest.getUserByMail("email@email.com"));
    }

    @Test
    public void getUserByMailShouldReturnTheUserIfFound() {
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        userRepository.save(user);

        User retrievedUser = underTest.getUserByMail(email);

        assertNotNull(retrievedUser);
        assertEquals(email, retrievedUser.getEmail());
    }

    @Test
    public void getAllUsersShouldReturnTheCorrectUsersList() {
        userRepository.save(User.builder().email("email@email.com").build());
        userRepository.save(User.builder().email("email2@email.com").build());

        List<User> retrievedUsers = underTest.getAllUsers();

        assertNotNull(retrievedUsers);
        assertEquals(2, retrievedUsers.size());
    }

    @Test
    public void updateUserShouldCorrectlyUpdateJustTheInputUser() {
        List<Role> roleList = List.of(Role.builder().roleType(RoleType.OWNER).build(), Role.builder().roleType(RoleType.OPERATOR).build());
        User firstUser = User.builder().email("email@email.com").name("name").surname("surname").username("username").roleList(roleList).build();
        User secondUser = User.builder().email("email2@email.com").name("name2").surname("surname2").username("username2").roleList(roleList).build();
        userRepository.save(firstUser);
        userRepository.save(secondUser);

        List<Role> updatedRoleList = List.of(Role.builder().roleType(RoleType.DEVELOPER).build());
        User userToUpdate = User.builder().name("updated_name").surname("updated_surname").username("updated_username").roleList(updatedRoleList).build();

        User updatedUser = underTest.updateUser(firstUser.getEmail(), userToUpdate);
        Optional<User> notUpdatedUser = userRepository.findById(secondUser.getEmail());

        assertNotNull(updatedUser);
        assertEquals(firstUser.getEmail(), updatedUser.getEmail());
        assertEquals(userToUpdate.getName(), updatedUser.getName());
        assertEquals(userToUpdate.getSurname(), updatedUser.getSurname());
        assertEquals(userToUpdate.getUsername(), updatedUser.getUsername());
        assertEquals(userToUpdate.getRoleList().size(), updatedUser.getRoleList().size());
        assertEquals(RoleType.DEVELOPER, updatedUser.getRoleList().get(0).getRoleType());
        assertTrue(notUpdatedUser.isPresent());
        assertEquals(secondUser.getName(), notUpdatedUser.get().getName());
        assertEquals(secondUser.getSurname(), notUpdatedUser.get().getSurname());
        assertEquals(secondUser.getUsername(), notUpdatedUser.get().getUsername());
    }

    @Test
    public void deleteUserShouldRemoveOnlyTheInputUser() {
        userRepository.save(User.builder().email("email@email.com").build());
        userRepository.save(User.builder().email("email2@email.com").build());

        underTest.deleteUserByEmail("email@email.com");

        List<User> userList = userRepository.findAll();

        assertEquals(1, userList.size());
        assertEquals("email2@email.com", userList.get(0).getEmail());
    }
}

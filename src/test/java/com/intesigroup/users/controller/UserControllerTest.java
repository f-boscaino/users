package com.intesigroup.users.controller;

import com.intesigroup.users.entity.Role;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.enums.RoleType;
import com.intesigroup.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Disabled
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void aGetAllRequestWithEmptyTableShouldReturnAnEmptyResponse() {
        restTestClient.get()
                .uri("http://localhost:%d/all".formatted(port))
                .exchange()
                .expectBody(String.class)
                .isEqualTo("[]");
    }

    @Test
    void aGetAllRequestWithUsersShouldReturnSomeResults() {
        User user = User.builder().email("email@email.com").build();
        userRepository.save(user);

        restTestClient.get()
                .uri("http://localhost:%d/all".formatted(port))
                .exchange()
                .expectBody(String.class)
                .isEqualTo("[{\"email\":\"email@email.com\",\"username\":null,\"name\":null,\"surname\":null,\"taxCode\":null,\"roleList\":[]}]");
    }

    @Test
    void aGetByEmailRequestWithDifferentEmailShouldThrow404() {
        User user = User.builder().email("email@email.com").build();
        userRepository.save(user);

        restTestClient.get()
                .uri("http://localhost:%d?email=%s".formatted(port, "different_email"))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void aGetByEmailRequestWithExistingEmailShouldReturnTheUser() {
        User firstUser = User.builder().email("email@email.com").build();
        User secondUser = User.builder().email("email2@email.com").build();
        userRepository.save(firstUser);
        userRepository.save(secondUser);

        restTestClient.get()
                .uri("http://localhost:%d?email=%s".formatted(port, firstUser.getEmail()))
                .exchange()
                .expectBody(String.class)
                .isEqualTo("{\"email\":\"email@email.com\",\"username\":null,\"name\":null,\"surname\":null,\"taxCode\":null,\"roleList\":[]}");
    }

    @Test
    void aPostRequestShouldCreateTheUser() {
        List<Role> rolelist = List.of(Role.builder().roleType(RoleType.OWNER).build(), Role.builder().roleType(RoleType.OPERATOR).build());
        User user = User.builder().email("email@email.com").username("username").name("name").surname("surname").taxCode("taxCode").roleList(rolelist).build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        restTestClient.post()
                .uri("http://localhost:%d".formatted(port))
                .body(jsonUser)
                .header("content-type", "application/json")
                .exchange()
                .expectBody(String.class)
                .isEqualTo(jsonUser);

        Optional<User> createdUser = userRepository.findById(user.getEmail());
        assertTrue(createdUser.isPresent());
        assertEquals(user.getUsername(), createdUser.get().getUsername());
    }

    @Test
    public void aPutRequestShouldUpdateTheUser() {
        List<Role> rolelist = List.of(Role.builder().roleType(RoleType.OWNER).build(), Role.builder().roleType(RoleType.OPERATOR).build());
        List<Role> updatedRolelist = List.of(Role.builder().roleType(RoleType.OPERATOR).build());
        User user = User.builder().email("email@email.com").username("username").name("name").surname("surname").taxCode("taxCode").roleList(rolelist).build();
        User updatedUser = User.builder().email("email@email.com").username("updated_username").name("updated_name").surname("updated_surname").taxCode("updated_taxCode").roleList(updatedRolelist).build();
        userRepository.save(user);
        String jsonUpdatedUser = new ObjectMapper().writeValueAsString(updatedUser);

        restTestClient.put()
                .uri("http://localhost:%d?email=%s".formatted(port, user.getEmail()))
                .body(jsonUpdatedUser)
                .header("content-type", "application/json")
                .exchange()
                .expectBody(String.class)
                .isEqualTo(jsonUpdatedUser);

        Optional<User> dbUser = userRepository.findById(user.getEmail());
        assertTrue(dbUser.isPresent());
        assertEquals(updatedUser.getEmail(), dbUser.get().getEmail());
        assertEquals(updatedUser.getUsername(), dbUser.get().getUsername());
        assertEquals(updatedUser.getName(), dbUser.get().getName());
        assertEquals(updatedUser.getSurname(), dbUser.get().getSurname());
        assertEquals(updatedUser.getTaxCode(), dbUser.get().getTaxCode());
    }

    @Test
    void aPutRequestWithNonExistingEmailShouldThrow404() {
        User user = User.builder().email("email@email.com").username("username").name("name").surname("surname").taxCode("taxCode").build();
        User updatedUser = User.builder().email("email@email.com").username("updated_username").name("updated_name").surname("updated_surname").taxCode("updated_taxCode").build();
        userRepository.save(user);
        String jsonUpdatedUser = new ObjectMapper().writeValueAsString(updatedUser);

        restTestClient.put()
                .uri("http://localhost:%d?email=%s".formatted(port, "different_email"))
                .body(jsonUpdatedUser)
                .header("content-type", "application/json")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void aDeleteRequestShouldDeleteTheUser() {
        User user = User.builder().email("email@email.com").username("username").name("name").surname("surname").taxCode("taxCode").build();
        userRepository.save(user);

        restTestClient.delete()
                .uri("http://localhost:%d?email=%s".formatted(port, user.getEmail()))
                .exchange();

        Optional<User> dbUser = userRepository.findById(user.getEmail());
        assertFalse(dbUser.isPresent());
    }

}
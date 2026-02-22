package com.intesigroup.users.service;

import com.intesigroup.users.entity.User;
import com.intesigroup.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final RabbitTemplate mockRabbitTemplate = mock(RabbitTemplate.class);

    @Test
    public void getAllUsersShouldReturnTheListOfUsers()  {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserService underTest = new UserService(mockUserRepository, mockRabbitTemplate);
        List<User> userList = List.of(User.builder().email("email@email.com").build());

        when(mockUserRepository.findAll()).thenReturn(userList);

        List<User> returnValue = underTest.getAllUsers();
        assertNotNull(returnValue);
        assertEquals(1, returnValue.size());
        assertEquals(userList.get(0).getEmail(), returnValue.get(0).getEmail());
    }

    @Test
    public void getUserByEmailShouldReturnTheUserIfThereIsAnExistingEmail()  {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserService underTest = new UserService(mockUserRepository, mockRabbitTemplate);
        String email = "email@email.com";
        User user = User.builder().email(email).build();

        when(mockUserRepository.findById(eq(email))).thenReturn(Optional.ofNullable(user));

        User returnValue = underTest.getUserByMail(email);
        assertNotNull(returnValue);
        assertEquals(email, returnValue.getEmail());
    }

    @Test
    public void getUserByEmailShouldReturnAnExceptionForANonExistingEmail()  {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserService underTest = new UserService(mockUserRepository, mockRabbitTemplate);

        when(mockUserRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                ()-> underTest.getUserByMail("email@email.com"));
    }

    @Test
    public void addUserShouldReturnTheAddedUser()  {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserService underTest = new UserService(mockUserRepository, mockRabbitTemplate);
        String email = "email@email.com";
        User user = User.builder().email(email).build();

        when(mockUserRepository.save(any())).thenReturn(user);
        doNothing().when(mockRabbitTemplate).convertAndSend(any(String.class), any(String.class), any(String.class));

        User returnValue = underTest.addUser(user);
        assertNotNull(returnValue);
        assertEquals(email, returnValue.getEmail());
    }

    @Test
    public void updateUserShouldReturnTheUpdatedUser()  {
        UserRepository mockUserRepository = mock(UserRepository.class);
        UserService underTest = new UserService(mockUserRepository, mockRabbitTemplate);
        String email = "email@email.com";
        User user = User.builder().email(email).build();

        when(mockUserRepository.save(any())).thenReturn(user);
        when(mockUserRepository.findById(eq(email))).thenReturn(Optional.ofNullable(user));

        User returnValue = underTest.updateUser(email, user);
        assertNotNull(returnValue);
        assertEquals(email, returnValue.getEmail());
    }



}
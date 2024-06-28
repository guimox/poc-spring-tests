package com.poc.test;

import com.poc.test.web.User;
import com.poc.test.web.UserRepository;
import com.poc.test.web.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(user.getId());
        assertTrue(result.isPresent());
        assertEquals(user.getName(), result.get().getName());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void createUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser() {
        User newUserDetails = new User();
        newUserDetails.setName("Jane Doe");
        newUserDetails.setEmail("jane@example.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user.getId(), newUserDetails);
        assertEquals(newUserDetails.getName(), result.getName());
        assertEquals(newUserDetails.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}

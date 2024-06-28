package com.poc.test;

import com.poc.test.web.User;
import com.poc.test.web.UserController;
import com.poc.test.web.UserRepository;
import com.poc.test.web.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).getUserById(user.getId());
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void updateUser() throws Exception {
        User newUserDetails = new User();
        newUserDetails.setName("Jane Doe");
        newUserDetails.setEmail("jane@example.com");

        when(userService.updateUser(eq(user.getId()), any(User.class))).thenReturn(newUserDetails);

        mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\", \"email\":\"jane@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        verify(userService, times(1)).updateUser(eq(user.getId()), any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(user.getId());

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(user.getId());
    }
}

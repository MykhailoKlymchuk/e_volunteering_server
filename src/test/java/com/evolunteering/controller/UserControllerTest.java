package com.evolunteering.controller;

import com.evolunteering.model.User;
import com.evolunteering.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers_Success() {
        List<User> users = new ArrayList<>();
        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsers();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).getUsers();
    }

    @Test
    void testGetUserByEmail_Success() {
        String email = "test@example.com";
        User user = new User();
        when(userService.getUser(email)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        String email = "test@example.com";
        when(userService.getUser(email)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void testGetUserByEmail_InternalError() {
        String email = "test@example.com";
        when(userService.getUser(email)).thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<?> response = userController.getUserByEmail(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error fetching user", response.getBody());
        verify(userService, times(1)).getUser(email);
    }

    @Test
    void testDeleteUser_Success() {
        String email = "test@example.com";
        doNothing().when(userService).deleteUser(email);

        ResponseEntity<String> response = userController.deleteUser(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(email);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String email = "test@example.com";
        doThrow(new UsernameNotFoundException("User not found")).when(userService).deleteUser(email);

        ResponseEntity<String> response = userController.deleteUser(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUser(email);
    }

    @Test
    void testDeleteUser_InternalError() {
        String email = "test@example.com";
        doThrow(new RuntimeException("Internal error")).when(userService).deleteUser(email);

        ResponseEntity<String> response = userController.deleteUser(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting user: Internal error", response.getBody());
        verify(userService, times(1)).deleteUser(email);
    }
}

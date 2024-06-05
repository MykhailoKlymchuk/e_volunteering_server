package com.evolunteering.service;

import com.evolunteering.exception.UserAlreadyExistsException;
import com.evolunteering.model.Role;
import com.evolunteering.model.User;
import com.evolunteering.repository.RoleRepository;
import com.evolunteering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        Role userRole = new Role("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1, result.getRoles().size());

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        //verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExistsException() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("test@example.com already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(roleRepository, times(0)).findByName(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testGetUsers_Success() {
        List<User> users = Collections.singletonList(new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser_Success() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteByEmail(email);

        userService.deleteUser(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).deleteByEmail(email);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.deleteUser(email);
        });

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(0)).deleteByEmail(email);
    }

    @Test
    void testGetUser_Success() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUser(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUser_UserNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser(email);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}

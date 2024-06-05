package com.evolunteering.controller;

import com.evolunteering.exception.UserAlreadyExistsException;
import com.evolunteering.model.User;
import com.evolunteering.request.LoginRequest;
import com.evolunteering.response.JwtResponse;
import com.evolunteering.security.jwt.JwtUtils;
import com.evolunteering.security.user.EVUserDetails;
import com.evolunteering.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private IUserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws UserAlreadyExistsException {
        User user = new User();
        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful!", response.getBody());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws UserAlreadyExistsException {
        User user = new User();
        doThrow(new UserAlreadyExistsException("User already exists")).when(userService).registerUser(user);

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginRequest request = new LoginRequest();
        Authentication authentication = mock(Authentication.class);
        EVUserDetails userDetails = mock(EVUserDetails.class);
        List<GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_USER");
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtTokenForUser(authentication)).thenReturn("mockedJWT");

        ResponseEntity<?> response = authController.authenticateUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getToken());
    }
}

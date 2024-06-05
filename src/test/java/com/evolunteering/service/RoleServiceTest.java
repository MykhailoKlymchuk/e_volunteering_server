package com.evolunteering.service;

import com.evolunteering.exception.RoleAlreadyExistException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles_Success() {
        List<Role> roles = Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN"));
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testCreateRole_Success() {
        Role role = new Role("ROLE_TEST");
        when(roleRepository.existsByName(role.getName())).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.createRole(new Role("TEST"));

        assertNotNull(result);
        assertEquals("ROLE_TEST", result.getName());
        verify(roleRepository, times(1)).existsByName(role.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testCreateRole_RoleAlreadyExistException() {
        when(roleRepository.existsByName("ROLE_TEST")).thenReturn(true);

        RoleAlreadyExistException exception = assertThrows(RoleAlreadyExistException.class, () -> {
            roleService.createRole(new Role("TEST"));
        });

        assertEquals("TEST role already exists", exception.getMessage());
        verify(roleRepository, times(1)).existsByName("ROLE_TEST");
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void testDeleteRole_Success() {
//        Long roleId = 1L;
//        doNothing().when(roleRepository).deleteById(roleId);
//
//        roleService.deleteRole(roleId);
//
//        verify(roleRepository, times(1)).deleteById(roleId);
    }

    @Test
    void testFindByName_Success() {
        Role role = new Role("ROLE_TEST");
        when(roleRepository.findByName("ROLE_TEST")).thenReturn(Optional.of(role));

        Role result = roleService.findByName("ROLE_TEST");

        assertNotNull(result);
        assertEquals("ROLE_TEST", result.getName());
        verify(roleRepository, times(1)).findByName("ROLE_TEST");
    }

    @Test
    void testRemoveUserFromRole_Success() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        user.setId(userId);
        Role role = new Role("ROLE_TEST");
        role.assignRoleToUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        User result = roleService.removeUserFromRole(userId, roleId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testRemoveUserFromRole_UserNotFound() {
        Long userId = 1L;
        Long roleId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            roleService.removeUserFromRole(userId, roleId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void testAssignRoleToUser_Success() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        user.setId(userId);
        Role role = new Role("ROLE_TEST");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        User result = roleService.assignRoleToUser(userId, roleId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testAssignRoleToUser_UserAlreadyExistsException() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        user.setId(userId);
        Role role = new Role("ROLE_TEST");
        role.assignRoleToUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            roleService.assignRoleToUser(userId, roleId);
        });

        assertEquals(user.getFirstName() + " is already assigned to the" + role.getName() + " role", exception.getMessage());
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void testRemoveAllUsersFromRole_Success() {
        Long roleId = 1L;
        Role role = new Role("ROLE_TEST");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.removeAllUsersFromRole(roleId);

        assertNotNull(result);
        assertEquals("ROLE_TEST", result.getName());
        verify(roleRepository, times(1)).save(role);
    }
}

package com.evolunteering.controller;

import com.evolunteering.exception.RoleAlreadyExistException;
import com.evolunteering.model.Role;
import com.evolunteering.model.User;
import com.evolunteering.service.IRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleControllerTest {
    @Mock
    private IRoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles_Success() {
        List<Role> roles = new ArrayList<>();
        when(roleService.getRoles()).thenReturn(roles);

        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(roles, response.getBody());
        verify(roleService, times(1)).getRoles();
    }

    @Test
    void testCreateRole_Success() throws RoleAlreadyExistException {
        Role role = new Role();
        when(roleService.createRole(role)).thenReturn(role);

        ResponseEntity<String> response = roleController.createRole(role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New role created successfully!", response.getBody());
        verify(roleService, times(1)).createRole(role);
    }

    @Test
    void testCreateRole_RoleAlreadyExists() throws RoleAlreadyExistException {
        Role role = new Role();
        doThrow(new RoleAlreadyExistException("Role already exists")).when(roleService).createRole(role);

        ResponseEntity<String> response = roleController.createRole(role);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Role already exists", response.getBody());
        verify(roleService, times(1)).createRole(role);
    }

    @Test
    void testDeleteRole_Success() {
        Long roleId = 1L;
        roleController.deleteRole(roleId);

        verify(roleService, times(1)).deleteRole(roleId);
    }

    @Test
    void testRemoveAllUsersFromRole_Success() {
        Long roleId = 1L;
        Role role = new Role();
        when(roleService.removeAllUsersFromRole(roleId)).thenReturn(role);

        Role response = roleController.removeAllUsersFromRole(roleId);

        assertNotNull(response);
        verify(roleService, times(1)).removeAllUsersFromRole(roleId);
    }

    @Test
    void testRemoveUserFromRole_Success() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        when(roleService.removeUserFromRole(userId, roleId)).thenReturn(user);

        User response = roleController.removeUserFromRole(userId, roleId);

        assertNotNull(response);
        verify(roleService, times(1)).removeUserFromRole(userId, roleId);
    }

    @Test
    void testAssignUserToRole_Success() {
        Long userId = 1L;
        Long roleId = 1L;
        User user = new User();
        when(roleService.assignRoleToUser(userId, roleId)).thenReturn(user);

        User response = roleController.assignUserToRole(userId, roleId);

        assertNotNull(response);
        verify(roleService, times(1)).assignRoleToUser(userId, roleId);
    }
}

package com.evolunteering.controller;

import com.evolunteering.exception.PhotoRetrievalException;
import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.Organization;
import com.evolunteering.response.OrganizationResponse;
import com.evolunteering.service.IOrganizationService;
import com.evolunteering.service.VolunteerInitiativeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrganizationControllerTest {
    @Mock
    private IOrganizationService organizationService;
    @Mock
    private VolunteerInitiativeService volunteerInitiativeService;

    @InjectMocks
    private OrganizationController organizationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewOrganization_Success() throws SQLException, IOException {
        MultipartFile photo = mock(MultipartFile.class);
        Organization savedOrganization = new Organization();
        when(organizationService.addNewOrganization(any(), any(), any(), any(), any(), any(), any())).thenReturn(savedOrganization);

        ResponseEntity<OrganizationResponse> response = organizationController.addNewOrganization(photo, "name", "location", "email", "phoneNumber", "bankAccountAddress", "description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(organizationService, times(1)).addNewOrganization(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void testGetRoomById_Success() {
        Long id = 1L;
        Organization organization = new Organization();
        Optional<Organization> optionalOrganization = Optional.of(organization);
        when(organizationService.getOrganizationById(id)).thenReturn(optionalOrganization);

        ResponseEntity<Optional<OrganizationResponse>> response = organizationController.getRoomById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        verify(organizationService, times(1)).getOrganizationById(id);
    }

    @Test
    void testGetRoomById_ResourceNotFound() {
        Long id = 1L;
        Optional<Organization> optionalOrganization = Optional.empty();
        when(organizationService.getOrganizationById(id)).thenReturn(optionalOrganization);

        assertThrows(ResourceNotFoundException.class, () -> organizationController.getRoomById(id));
        verify(organizationService, times(1)).getOrganizationById(id);
    }

    @Test
    void testGetAllOrganizations_Success() throws SQLException {
        List<Organization> organizations = new ArrayList<>();
        when(organizationService.getAllOrganizations()).thenReturn(organizations);

        ResponseEntity<List<OrganizationResponse>> response = organizationController.getAllOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(organizationService, times(1)).getAllOrganizations();
    }

    @Test
    void testGetAllOrganizations_Exception() throws SQLException {

    }

    @Test
    void testGetAllOrganizationLocations_Success() {
        List<String> locations = new ArrayList<>();
        when(organizationService.findDistinctLocations()).thenReturn(locations);

        List<String> response = organizationController.getAllOrganizationLocations();

        assertNotNull(response);
        assertEquals(locations, response);
        verify(organizationService, times(1)).findDistinctLocations();
    }
}

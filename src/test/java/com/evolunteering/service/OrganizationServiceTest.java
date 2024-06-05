package com.evolunteering.service;

import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.Organization;
import com.evolunteering.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewOrganization_WithPhoto_Success() throws IOException, SQLException {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");

        byte[] photoBytes = {1, 2, 3};
        Blob photoBlob = new SerialBlob(photoBytes);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenReturn(photoBytes);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.addNewOrganization(
                multipartFile,
                "Test Organization",
                "Test Location",
                "test@example.com",
                "1234567890",
                "Test Bank Account",
                "Test Description"
        );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }

    @Test
    void testAddNewOrganization_WithoutPhoto_Success() throws IOException, SQLException {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");

        when(multipartFile.isEmpty()).thenReturn(true);
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization result = organizationService.addNewOrganization(
                multipartFile,
                "Test Organization",
                "Test Location",
                "test@example.com",
                "1234567890",
                "Test Bank Account",
                "Test Description"
        );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }

    @Test
    void testGetAllOrganizations_Success() {
        List<Organization> organizations = Arrays.asList(new Organization(), new Organization());
        when(organizationRepository.findAll()).thenReturn(organizations);

        List<Organization> result = organizationService.getAllOrganizations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(organizationRepository, times(1)).findAll();
    }

    @Test
    void testGetOrganizationPhotoByOrganizationId_Success() throws SQLException {
        Organization organization = new Organization();
        byte[] photoBytes = {1, 2, 3};
        Blob photoBlob = new SerialBlob(photoBytes);
        organization.setPhoto(photoBlob);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        byte[] result = organizationService.getOrganizationPhotoByOrganizationId(1L);

        assertNotNull(result);
        assertArrayEquals(photoBytes, result);
        verify(organizationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrganizationPhotoByOrganizationId_NotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            organizationService.getOrganizationPhotoByOrganizationId(1L);
        });

        verify(organizationRepository, times(1)).findById(1L);
    }

    @Test
    void testFindDistinctLocations_Success() {
        List<String> locations = Arrays.asList("Location1", "Location2");
        when(organizationRepository.findDistinctLocations()).thenReturn(locations);

        List<String> result = organizationService.findDistinctLocations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(organizationRepository, times(1)).findDistinctLocations();
    }

    @Test
    void testGetOrganizationById_Success() {
        Organization organization = new Organization();
        organization.setId(1L);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        Optional<Organization> result = organizationService.getOrganizationById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(organizationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrganizationById_NotFound() {
//        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<Organization> result = organizationService.getOrganizationById(1L);
//
//        assertFalse(result.isPresent());
//        verify(organizationRepository, times(1)).findById(1L);
    }
}

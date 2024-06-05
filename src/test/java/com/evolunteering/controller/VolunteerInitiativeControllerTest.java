package com.evolunteering.controller;

import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.response.VolunteerInitiativeResponse;
import com.evolunteering.service.IVolunteerInitiativeService;
import com.evolunteering.service.OrganizationService;
import com.evolunteering.service.VITypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VolunteerInitiativeControllerTest {
    @Mock
    private OrganizationService organizationService;

    @Mock
    private VITypeService viTypeService;

    @Mock
    private IVolunteerInitiativeService volunteerInitiativeService;

    @InjectMocks
    private VolunteerInitiativeController volunteerInitiativeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewVolunteerInitiative_Success() throws SQLException, IOException {
        VolunteerInitiative initiative = new VolunteerInitiative();
        initiative.setId(1L);
        initiative.setName("Test Initiative");
        initiative.setVolunteerInitiativeType("Type");
        initiative.setRequiredAmount(BigDecimal.valueOf(1000));
        initiative.setJarUrl("http://example.com/jar");
        initiative.setJarUrlStats("http://example.com/stats");
        initiative.setDescription("Test Description");

        when(volunteerInitiativeService.addNewVolunteerInitiative(
                anyString(), anyString(), any(BigDecimal.class), anyString(), anyString(), anyString()
        )).thenReturn(initiative);

        ResponseEntity<VolunteerInitiativeResponse> response = volunteerInitiativeController.addNewVolunteerInitiative(
                "Test Initiative", "Type", BigDecimal.valueOf(1000), "http://example.com/jar", "http://example.com/stats", "Test Description"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(initiative.getId(), response.getBody().getId());
        verify(volunteerInitiativeService, times(1)).addNewVolunteerInitiative(
                anyString(), anyString(), any(BigDecimal.class), anyString(), anyString(), anyString()
        );
    }

    @Test
    void testGetAllVolunteerInitiativeType_Success() {
        List<String> types = List.of("Type1", "Type2");
        when(viTypeService.findDistinctTypes()).thenReturn(types);

        List<String> response = volunteerInitiativeController.getAllVolunteerInitiativeType();

        assertEquals(types, response);
        verify(viTypeService, times(1)).findDistinctTypes();
    }

    @Test
    void testGetAllVolunteerInitiatives_Success() throws SQLException {
        List<VolunteerInitiative> initiatives = new ArrayList<>();
        when(volunteerInitiativeService.getAllVolunteerInitiatives()).thenReturn(initiatives);

        ResponseEntity<List<VolunteerInitiativeResponse>> response = volunteerInitiativeController.getAllVolunteerInitiatives();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(volunteerInitiativeService, times(1)).getAllVolunteerInitiatives();
    }

    @Test
    void testCloseVI_Success() {
        Long viId = 1L;
        doNothing().when(volunteerInitiativeService).closeVI(viId);

        ResponseEntity<String> response = volunteerInitiativeController.closeVI(viId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Volunteer Initiative closed successfully.", response.getBody());
        verify(volunteerInitiativeService, times(1)).closeVI(viId);
    }

    @Test
    void testCloseVI_NotFound() {
        Long viId = 1L;
        doThrow(new ResourceNotFoundException("Volunteer Initiative not found")).when(volunteerInitiativeService).closeVI(viId);

        ResponseEntity<String> response = volunteerInitiativeController.closeVI(viId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Volunteer Initiative not found.", response.getBody());
        verify(volunteerInitiativeService, times(1)).closeVI(viId);
    }

    @Test
    void testGetVolunteerInitiativeById_Success() {
        Long viId = 1L;
        VolunteerInitiative initiative = new VolunteerInitiative();
        when(volunteerInitiativeService.getVolunteerInitiativeById(viId)).thenReturn(Optional.of(initiative));

        ResponseEntity<Optional<VolunteerInitiativeResponse>> response = volunteerInitiativeController.getVolunteerInitiativeById(viId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        verify(volunteerInitiativeService, times(1)).getVolunteerInitiativeById(viId);
    }

    @Test
    void testGetVolunteerInitiativeById_NotFound() {
        Long viId = 1L;
        when(volunteerInitiativeService.getVolunteerInitiativeById(viId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            volunteerInitiativeController.getVolunteerInitiativeById(viId);
        });

        verify(volunteerInitiativeService, times(1)).getVolunteerInitiativeById(viId);
    }
}

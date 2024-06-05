package com.evolunteering.service;

import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.repository.VolunteerInitiativeRepository;
import com.evolunteering.utils.CsvWriter;
import com.evolunteering.utils.PriorityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VolunteerInitiativeServiceTest {

    @Mock
    private VolunteerInitiativeRepository volunteerInitiativeRepository;

    @Mock
    private VITypeService viTypeService;

    @Mock
    private CsvWriter csvWriter;

    @InjectMocks
    private VolunteerInitiativeService volunteerInitiativeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVolunteerInitiativesByOrganizationId() {
        Long organizationId = 1L;
        List<VolunteerInitiative> initiatives = Arrays.asList(new VolunteerInitiative(), new VolunteerInitiative());
        when(volunteerInitiativeRepository.findVolunteerInitiativesByOrganizationId(organizationId)).thenReturn(initiatives);

        List<VolunteerInitiative> result = volunteerInitiativeService.getAllVolunteerInitiativesByOrganizationId(organizationId);

        assertEquals(2, result.size());
        verify(volunteerInitiativeRepository, times(1)).findVolunteerInitiativesByOrganizationId(organizationId);
    }

    @Test
    void testAddNewVolunteerInitiative() {
        String name = "Education Initiative";
        String volunteerInitiativeType = "Education";
        BigDecimal requiredAmount = new BigDecimal("1000");
        String jarUrl = "http://example.com/jar";
        String jarUrlStats = "http://example.com/stats";
        String description = "An initiative to improve education.";

        when(viTypeService.getCoeffOfNecessityByVolunteerInitiativeTypeName(volunteerInitiativeType)).thenReturn(1.5);
        when(volunteerInitiativeRepository.findAll()).thenReturn(Arrays.asList());

        VolunteerInitiative savedInitiative = new VolunteerInitiative();
        when(volunteerInitiativeRepository.save(any(VolunteerInitiative.class))).thenReturn(savedInitiative);

        VolunteerInitiative result = volunteerInitiativeService.addNewVolunteerInitiative(
                name, volunteerInitiativeType, requiredAmount, jarUrl, jarUrlStats, description);

        assertNotNull(result);
        verify(volunteerInitiativeRepository, times(1)).save(any(VolunteerInitiative.class));
    }

    @Test
    void testGetAllVolunteerInitiatives() {
        VolunteerInitiative initiative1 = new VolunteerInitiative();
        initiative1.setCoeffOfNecessity(2.0);
        VolunteerInitiative initiative2 = new VolunteerInitiative();
        initiative2.setCoeffOfNecessity(3.0);

        when(volunteerInitiativeRepository.findAll()).thenReturn(Arrays.asList(initiative1, initiative2));

        List<VolunteerInitiative> result = volunteerInitiativeService.getAllVolunteerInitiatives();

        assertEquals(2, result.size());
        assertEquals(initiative2, result.get(0)); // Sorted by coeffOfNecessity in descending order
        assertEquals(initiative1, result.get(1));
        verify(volunteerInitiativeRepository, times(1)).findAll();
    }

    @Test
    void testCloseVI() {
        Long viId = 1L;
        VolunteerInitiative initiative = new VolunteerInitiative();
        when(volunteerInitiativeRepository.findById(viId)).thenReturn(Optional.of(initiative));

        volunteerInitiativeService.closeVI(viId);

        //assertTrue(initiative.isClosed());
        verify(volunteerInitiativeRepository, times(1)).findById(viId);
        verify(volunteerInitiativeRepository, times(1)).save(initiative);
    }

    @Test
    void testGetVolunteerInitiativeById() {
        Long viId = 1L;
        VolunteerInitiative initiative = new VolunteerInitiative();
        when(volunteerInitiativeRepository.findById(viId)).thenReturn(Optional.of(initiative));

        Optional<VolunteerInitiative> result = volunteerInitiativeService.getVolunteerInitiativeById(viId);

        assertTrue(result.isPresent());
        assertEquals(initiative, result.get());
        verify(volunteerInitiativeRepository, times(1)).findById(viId);
    }
}

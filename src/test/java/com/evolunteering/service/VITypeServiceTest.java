package com.evolunteering.service;

import com.evolunteering.model.VolunteerInitiativeType;
import com.evolunteering.repository.VolunteerInitiativeTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VITypeServiceTest {

    @Mock
    private VolunteerInitiativeTypeRepository initiativeTypeRepository;

    @InjectMocks
    private VITypeService viTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCoeffOfNecessityByVolunteerInitiativeTypeName_Success() {
        String typeName = "Education";
        VolunteerInitiativeType initiativeType = new VolunteerInitiativeType();
        initiativeType.setName(typeName);
        initiativeType.setCoeff(1.5);

        when(initiativeTypeRepository.findVolunteerInitiativeTypeByName(typeName)).thenReturn(initiativeType);

        double result = viTypeService.getCoeffOfNecessityByVolunteerInitiativeTypeName(typeName);

        assertEquals(1.5, result, 0.001);
        verify(initiativeTypeRepository, times(1)).findVolunteerInitiativeTypeByName(typeName);
    }

    @Test
    void testFindDistinctTypes_Success() {
        List<String> types = Arrays.asList("Education", "Health", "Environment");

        when(initiativeTypeRepository.findDistinctTypes()).thenReturn(types);

        List<String> result = viTypeService.findDistinctTypes();

        assertEquals(3, result.size());
        assertEquals("Education", result.get(0));
        assertEquals("Health", result.get(1));
        assertEquals("Environment", result.get(2));
        verify(initiativeTypeRepository, times(1)).findDistinctTypes();
    }
}

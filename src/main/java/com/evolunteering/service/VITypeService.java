package com.evolunteering.service;

import com.evolunteering.repository.VolunteerInitiativeRepository;
import com.evolunteering.repository.VolunteerInitiativeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VITypeService implements IVolunteerInitiativeTypeService {

    private final VolunteerInitiativeTypeRepository initiativeTypeRepository;

    @Override
    public double getCoeffOfNecessityByVolunteerInitiativeTypeName(String volunteerInitiativeType) {
        return initiativeTypeRepository.findVolunteerInitiativeTypeByName(volunteerInitiativeType).getCoeff();

    }

    @Override
    public List<String> findDistinctTypes() {
        return initiativeTypeRepository.findDistinctTypes();
    }
}
package com.evolunteering.service;

import java.util.List;

public interface IVolunteerInitiativeTypeService {
    double getCoeffOfNecessityByVolunteerInitiativeTypeName(String volunteerInitiativeType);

    List<String> findDistinctTypes();
}

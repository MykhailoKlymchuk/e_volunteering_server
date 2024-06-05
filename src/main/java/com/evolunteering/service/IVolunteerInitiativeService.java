package com.evolunteering.service;

import com.evolunteering.model.VolunteerInitiative;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IVolunteerInitiativeService {
    List<VolunteerInitiative> getAllVolunteerInitiativesByOrganizationId(Long organizationId);

    VolunteerInitiative addNewVolunteerInitiative(
            String name,
            String volunteerInitiativeType,
            BigDecimal requiredAmount,
            String jarUrl,
            String jarUrlStats,
            String description);

    List<VolunteerInitiative> getAllVolunteerInitiatives();

    void closeVI(Long viId);

    Optional<VolunteerInitiative> getVolunteerInitiativeById(Long id);
}

package com.evolunteering.repository;

import com.evolunteering.model.VolunteerInitiative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerInitiativeRepository extends JpaRepository<VolunteerInitiative, Long> {

    List<VolunteerInitiative> findVolunteerInitiativesByOrganizationId(Long organizationId);

}

package com.evolunteering.repository;

import com.evolunteering.model.VolunteerInitiativeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerInitiativeTypeRepository
        extends JpaRepository<VolunteerInitiativeType, Long> {

    VolunteerInitiativeType findVolunteerInitiativeTypeByName(String name);

    @Query("SELECT DISTINCT viType.name FROM VolunteerInitiativeType viType")
    List<String> findDistinctTypes();
}

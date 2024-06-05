package com.evolunteering.repository;

import com.evolunteering.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("SELECT DISTINCT o.location FROM Organization o")

    List<String> findDistinctLocations();

    Optional<Organization> findById(Long id);
}

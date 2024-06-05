package com.evolunteering.service;

import com.evolunteering.model.Organization;
import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.repository.VolunteerInitiativeRepository;
import com.evolunteering.utils.CsvWriter;
import com.evolunteering.utils.PriorityCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerInitiativeService implements IVolunteerInitiativeService {

    private final VolunteerInitiativeRepository volunteerInitiativeRepository;
    private final VITypeService viTypeService;
    private final CsvWriter csvWriter;

    @Override
    public List<VolunteerInitiative> getAllVolunteerInitiativesByOrganizationId(Long organizationId) {
        return volunteerInitiativeRepository.findVolunteerInitiativesByOrganizationId(organizationId);
    }

    @Override
    public VolunteerInitiative addNewVolunteerInitiative(
            String name,
            String volunteerInitiativeType,
            BigDecimal requiredAmount,
            String jarUrl,
            String jarUrlStats,
            String description) {
        VolunteerInitiative initiative = new VolunteerInitiative();

        initiative.setName(name);
        initiative.setVolunteerInitiativeType(volunteerInitiativeType);
        initiative.setRequiredAmount(requiredAmount);

        initiative.setJarUrl(jarUrl);
        initiative.setJarUrlStats(jarUrlStats);
        initiative.setPublicationDate(LocalDate.now());
        initiative.setDeadlineDate(LocalDate.now().plusDays(7));
        initiative.setDescription(description);

        //todo
        initiative.setCoeffOfNecessity(
                PriorityCalculator.calculatePriorityCoefficient(
                        initiative,
                        viTypeService.getCoeffOfNecessityByVolunteerInitiativeTypeName(
                                volunteerInitiativeType
                        ),
                        getAllVolunteerInitiatives()
                )
        );


        return volunteerInitiativeRepository.save(initiative);
    }

    @Override
    public List<VolunteerInitiative> getAllVolunteerInitiatives() {

        List<VolunteerInitiative> initiatives = volunteerInitiativeRepository.findAll();
        //todo
        return initiatives.stream()
                .sorted((initiative1, initiative2) -> Double.compare(initiative2.getCoeffOfNecessity(), initiative1.getCoeffOfNecessity()))
                .collect(Collectors.toList());
    }

    @Override
    public void closeVI(Long viId) {
        VolunteerInitiative volunteerInitiative = volunteerInitiativeRepository.findById(viId)
                .orElseThrow(() -> new RuntimeException("VolunteerInitiative not found with id " + viId));
        volunteerInitiative.Closed();
        volunteerInitiativeRepository.save(volunteerInitiative);
    }

    @Override
    public Optional<VolunteerInitiative> getVolunteerInitiativeById(Long id) {
        return Optional.of(volunteerInitiativeRepository.findById(id).get());

    }




}

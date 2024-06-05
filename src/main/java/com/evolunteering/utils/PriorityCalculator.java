package com.evolunteering.utils;

import com.evolunteering.model.VolunteerInitiative;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class PriorityCalculator {

    public static double calculatePriorityCoefficient(
            VolunteerInitiative initiative,
            double strategicImportance,
            List<VolunteerInitiative> allVolunteerInitiatives) {
        double impactOnUsers = initiative.getRequiredAmount().doubleValue() / 1000;
        double deadline = ChronoUnit.DAYS.between(initiative.getPublicationDate(), initiative.getDeadlineDate());

        double economicEffect = deadline != 0 ?
                initiative.getRequiredAmount().doubleValue() / deadline :
                0.0;

        double externalFactors = countInitiativesByType(allVolunteerInitiatives, initiative.getVolunteerInitiativeType()) != 0 ?
                (double) allVolunteerInitiatives.size() / countInitiativesByType(allVolunteerInitiatives, initiative.getVolunteerInitiativeType()) :
                0.0;

        CsvWriter.writeToCsv(initiative, strategicImportance, impactOnUsers, deadline, economicEffect, externalFactors);


        return (strategicImportance * impactOnUsers * economicEffect) + (deadline * externalFactors);
    }


    public static long countInitiativesByType(List<VolunteerInitiative> allVolunteerInitiatives, String volunteerInitiativeType) {
        return allVolunteerInitiatives.stream()
                .filter(vi -> vi.getVolunteerInitiativeType().equals(volunteerInitiativeType))
                .count();
    }

}


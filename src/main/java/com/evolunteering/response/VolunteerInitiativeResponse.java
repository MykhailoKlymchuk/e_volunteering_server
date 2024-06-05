package com.evolunteering.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerInitiativeResponse {

    private Long id;

    private String name;
    private String volunteerInitiativeType;

    private BigDecimal requiredAmount;

    private boolean isСlosed;

    private double coeffOfNecessity;

    private String jarUrlStats;

    private String jarUrl;

    private LocalDate publicationDate;
    private LocalDate deadlineDate;

    private String description;


    private OrganizationResponse organization;

    public VolunteerInitiativeResponse(
            Long id,
            String name,
            String volunteerInitiativeType,
            BigDecimal requiredAmount,
            boolean isСlosed,
            double coeffOfNecessity,
            String jarUrl,
            String jarUrlStats,
            LocalDate publicationDate,
            LocalDate deadlineDate,
            String description) {
        this.id = id;
        this.name = name;
        this.volunteerInitiativeType = volunteerInitiativeType;
        this.requiredAmount = requiredAmount;
        this.isСlosed = isСlosed;
        this.coeffOfNecessity = coeffOfNecessity;
        this.jarUrl = jarUrl;
        this.jarUrlStats = jarUrlStats;
        this.publicationDate = publicationDate;
        this.deadlineDate = deadlineDate;
        this.description = description;
    }
}

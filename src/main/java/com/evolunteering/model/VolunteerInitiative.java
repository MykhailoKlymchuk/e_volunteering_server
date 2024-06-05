package com.evolunteering.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerInitiative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long Id;


    @Column(name = "name")
    private String name;

    @Column(name = "volunteer_initiative_type")
    private String volunteerInitiativeType;

    @Column(name = "required_amount")
    private BigDecimal requiredAmount;

    @Column(name = "is_closed")
    private boolean isСlosed = false;

    @Column(name = "coeff_of_necessity")
    private double coeffOfNecessity;

    @Column(name = "jar_url")
    private String jarUrl;

    @Column(name = "jar_url_stats")
    private String jarUrlStats;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "volunteerInitiative", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reports;

    public void Closed() {
        this.isСlosed = true;
    }
}

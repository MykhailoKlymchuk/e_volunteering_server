package com.evolunteering.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Blob photo;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "bank_account_address")
    private String bankAccountAddress;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VolunteerInitiative> volunteerInitiatives = new ArrayList<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Receipt> receipts = new ArrayList<>();

    public Organization() {
        this.volunteerInitiatives = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.receipts = new ArrayList<>();
    }

    public void addVolunteerInitiative(VolunteerInitiative volunteerInitiative) {
        volunteerInitiatives.add(volunteerInitiative);
        volunteerInitiative.setOrganization(this);
    }

    public void addReport(Report report) {
        reports.add(report);
        report.setOrganization(this);
    }

    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
        receipt.setOrganization(this);
    }
}

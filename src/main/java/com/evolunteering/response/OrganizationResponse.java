package com.evolunteering.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.List;

@Data
@NoArgsConstructor
public class OrganizationResponse {

    private Long id;
    private String name;
    private String location;
    private String photo;
    private String email;
    private String phoneNumber;
    private String bankAccountAddress;
    private String description;
    private List<VolunteerInitiativeResponse> volunteerInitiatives;

    public OrganizationResponse(Long id, String name, String location, String email, String phoneNumber, String bankAccountAddress, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bankAccountAddress = bankAccountAddress;
        this.description = description;
    }

    public OrganizationResponse(Long id,
                                byte[] photoByte,
                                String name,
                                String location,
                                String email,
                                String phoneNumber,
                                String bankAccountAddress,
                                String description,
                                List<VolunteerInitiativeResponse> volunteerInitiatives) {
        this.id = id;
        this.photo = photoByte != null ? Base64.encodeBase64String(photoByte) : null;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bankAccountAddress = bankAccountAddress;
        this.description = description;
        this.volunteerInitiatives = volunteerInitiatives;
    }
}

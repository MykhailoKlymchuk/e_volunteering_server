package com.evolunteering.controller;

import com.evolunteering.exception.PhotoRetrievalException;
import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.Organization;
import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.response.OrganizationResponse;
import com.evolunteering.response.VolunteerInitiativeResponse;
import com.evolunteering.service.IOrganizationService;
import com.evolunteering.service.VolunteerInitiativeService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final IOrganizationService organizationService;
    private final VolunteerInitiativeService volunteerInitiativeService;


    @PostMapping("/add/new-organization")
    public ResponseEntity<OrganizationResponse> addNewOrganization(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("bankAccountAddress") String bankAccountAddress,
            @RequestParam("description") String description) throws SQLException, IOException {
        System.out.print(location);
        Organization savedOrganization = organizationService.addNewOrganization(photo, name, location, email, phoneNumber, bankAccountAddress, description);
        OrganizationResponse response = new OrganizationResponse(
                savedOrganization.getId(),
                savedOrganization.getName(),
                savedOrganization.getLocation(),
                savedOrganization.getEmail(),
                savedOrganization.getPhoneNumber(),
                savedOrganization.getBankAccountAddress(),
                savedOrganization.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/{Id}")
    public ResponseEntity<Optional<OrganizationResponse>> getRoomById(@PathVariable Long Id){
        Optional<Organization> theOrganization = organizationService.getOrganizationById(Id);
        return theOrganization.map(org -> {
            OrganizationResponse organizationResponse = getOrganizationResponse(org);
            return  ResponseEntity.ok(Optional.of(organizationResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @GetMapping("/organization/locations")
    public List<String> getAllOrganizationLocations() {
        return organizationService.findDistinctLocations();
    }

    @GetMapping("/all-organizations")
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() throws SQLException {
        List<Organization> organizations = organizationService.getAllOrganizations();
        List<OrganizationResponse> organizationResponses = new ArrayList<>();
        for (Organization organization : organizations) {
            byte[] photoBytes = organizationService.getOrganizationPhotoByOrganizationId(organization.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                OrganizationResponse organizationResponse = getOrganizationResponse(organization);
                organizationResponse.setPhoto(base64Photo);
                organizationResponses.add(organizationResponse);
            }
        }
        return ResponseEntity.ok(organizationResponses);
    }

    private OrganizationResponse getOrganizationResponse(Organization organization) {
        List<VolunteerInitiative> volunteerInitiatives = getAllVolunteerInitiativesByOrganizationId(organization.getId());
        List<VolunteerInitiativeResponse> volunteerInitiativeResponses = volunteerInitiatives
                .stream()
                .map(vi -> new VolunteerInitiativeResponse(
                                vi.getId(),
                                vi.getName(),
                                vi.getVolunteerInitiativeType(),
                                vi.getRequiredAmount(),
                                vi.isСlosed(),
                                vi.getCoeffOfNecessity(),
                                vi.getJarUrl(),
                                vi.getJarUrlStats(),
                                vi.getPublicationDate(),
                                vi.getDeadlineDate(),
                                vi.getDescription()
                        )
                ).toList();
        byte[] photoBytes = null;
        Blob photoBlob = organization.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("error retrieving photo");
            }
        }
        return new OrganizationResponse(
                organization.getId(),
                photoBytes,
                organization.getName(),
                organization.getLocation(),
                organization.getEmail(),
                organization.getPhoneNumber(),
                organization.getBankAccountAddress(),
                organization.getDescription(),
                volunteerInitiativeResponses
        );
    }

    private List<VolunteerInitiative> getAllVolunteerInitiativesByOrganizationId(Long organizationId) {
        return volunteerInitiativeService.getAllVolunteerInitiativesByOrganizationId(organizationId);
    }
}

package com.evolunteering.controller;

import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.Organization;
import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.response.OrganizationResponse;
import com.evolunteering.response.VolunteerInitiativeResponse;
import com.evolunteering.service.IVolunteerInitiativeService;
import com.evolunteering.service.OrganizationService;
import com.evolunteering.service.VITypeService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vis")
public class VolunteerInitiativeController {

    private final OrganizationService organizationService;
    private final VITypeService viTypeService;
    private final IVolunteerInitiativeService volunteerInitiativeService;


    @PostMapping("/add/new-vi")
    public ResponseEntity<VolunteerInitiativeResponse> addNewVolunteerInitiative(
            @RequestParam("name") String name,
            @RequestParam("volunteerInitiativeType") String volunteerInitiativeType,
            @RequestParam("requiredAmount") BigDecimal requiredAmount,
            @RequestParam("jarUrl") String jarUrl,
            @RequestParam("jarUrlStats") String jarUrlStats,
            @RequestParam("description") String description) throws SQLException, IOException {

        VolunteerInitiative savedVolunteerInitiative = volunteerInitiativeService
                .addNewVolunteerInitiative(
                        name,
                        volunteerInitiativeType,
                        requiredAmount,
                        jarUrl,
                        jarUrlStats,
                        description
                );

        VolunteerInitiativeResponse response = new VolunteerInitiativeResponse(
                savedVolunteerInitiative.getId(),
                savedVolunteerInitiative.getName(),
                savedVolunteerInitiative.getVolunteerInitiativeType(),
                savedVolunteerInitiative.getRequiredAmount(),
                savedVolunteerInitiative.isСlosed(),
                savedVolunteerInitiative.getCoeffOfNecessity(),
                savedVolunteerInitiative.getJarUrl(),
                savedVolunteerInitiative.getJarUrlStats(),
                savedVolunteerInitiative.getPublicationDate(),
                savedVolunteerInitiative.getDeadlineDate(),
                savedVolunteerInitiative.getDescription()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vi/types")
    public List<String> getAllVolunteerInitiativeType() {
        return viTypeService.findDistinctTypes();
    }


    @GetMapping("/all-volunteer-initiative")
    public ResponseEntity<List<VolunteerInitiativeResponse>> getAllVolunteerInitiatives() throws SQLException {
        List<VolunteerInitiative> initiatives = volunteerInitiativeService.getAllVolunteerInitiatives();
        List<VolunteerInitiativeResponse> volunteerInitiativeResponses = new ArrayList<>();
        for (VolunteerInitiative initiative : initiatives) {
            VolunteerInitiativeResponse volunteerInitiativeResponse =
                    getVolunteerInitiativeResponse(initiative);
            volunteerInitiativeResponses.add(volunteerInitiativeResponse);
        }
        return ResponseEntity.ok(volunteerInitiativeResponses);
    }

    private VolunteerInitiativeResponse getVolunteerInitiativeResponse(VolunteerInitiative initiative) {
        return new VolunteerInitiativeResponse(
                initiative.getId(),
                initiative.getName(),
                initiative.getVolunteerInitiativeType(),
                initiative.getRequiredAmount(),
                initiative.isСlosed(),
                initiative.getCoeffOfNecessity(),
                initiative.getJarUrl(),
                initiative.getJarUrlStats(),
                initiative.getPublicationDate(),
                initiative.getDeadlineDate(),
                initiative.getDescription()
                //getOrganizationResponse(initiative)
        );
    }

    private OrganizationResponse getOrganizationResponse(VolunteerInitiative initiative) {
        Organization organization = organizationService.getOrganizationById(initiative.getOrganization().getId()).get();
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getLocation(),
                organization.getEmail(),
                organization.getPhoneNumber(),
                organization.getBankAccountAddress(),
                organization.getDescription()
        );
    }

    @PutMapping("/vi/{viId}/close")
    public ResponseEntity<String> closeVI(@PathVariable Long viId) {
        try {
            volunteerInitiativeService.closeVI(viId);
            return ResponseEntity.ok("Volunteer Initiative closed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Volunteer Initiative not found.");
        }
    }

    @GetMapping("/vi/{Id}")
    public ResponseEntity<Optional<VolunteerInitiativeResponse>> getVolunteerInitiativeById(@PathVariable Long Id){
        Optional<VolunteerInitiative> theVolunteerInitiative = volunteerInitiativeService.getVolunteerInitiativeById(Id);
        return theVolunteerInitiative.map(vi -> {
            VolunteerInitiativeResponse volunteerInitiativeResponse = getVolunteerInitiativeResponse(vi);
            return  ResponseEntity.ok(Optional.of(volunteerInitiativeResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }


}

package com.evolunteering.service;

import com.evolunteering.exception.ResourceNotFoundException;
import com.evolunteering.model.Organization;
import com.evolunteering.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public Organization addNewOrganization(MultipartFile file,
                                           String name,
                                           String location,
                                           String email,
                                           String phoneNumber,
                                           String bankAccountAddress,
                                           String description) throws SQLException, IOException {
        Organization organization = new Organization();
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            organization.setPhoto(photoBlob);
        }
        organization.setName(name);
        organization.setLocation(location);
        organization.setEmail(email);
        organization.setPhoneNumber(phoneNumber);
        organization.setBankAccountAddress(bankAccountAddress);
        organization.setDescription(description);
        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public byte[] getOrganizationPhotoByOrganizationId(Long organizationId) throws SQLException {
        Optional<Organization> theOrganization = organizationRepository.findById(organizationId);
        if (theOrganization.isEmpty())
            throw new ResourceNotFoundException("Organization not found!");
        Blob photoBlob = theOrganization.get().getPhoto();
        if (photoBlob != null)
            return photoBlob.getBytes(1, (int) photoBlob.length());
        return null;
    }

    @Override
    public List<String> findDistinctLocations() {
        return organizationRepository.findDistinctLocations();
    }

    @Override
    public Optional<Organization> getOrganizationById(Long organizationId) {
        return Optional.of(organizationRepository.findById(organizationId).get());

    }

}

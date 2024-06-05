package com.evolunteering.service;

import com.evolunteering.model.Organization;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IOrganizationService {
    Organization addNewOrganization(MultipartFile photo, String name,  String location, String email, String phoneNumber, String bankAccountAddress, String description) throws SQLException, IOException;

    List<Organization> getAllOrganizations();

    byte[] getOrganizationPhotoByOrganizationId(Long organizationId) throws SQLException;

    List<String> findDistinctLocations();
    Optional<Organization> getOrganizationById(Long organizationId);
}

package com.evolunteering.utils;

import com.evolunteering.model.VolunteerInitiative;
import com.evolunteering.service.VITypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.evolunteering.utils.PriorityCalculator.countInitiativesByType;

@Service
@RequiredArgsConstructor
public class CsvWriter {

    private final VITypeService viTypeService;

    private static final String CSV_FILE_PATH = "initiative_data.csv";

    public static void writeToCsv(VolunteerInitiative initiative,
                                  double strategicImportance,
                                  double impactOnUsers,
                                  double deadline,
                                  double economicEffect,
                                  double externalFactors) {
        try {
            File file = new File(CSV_FILE_PATH);
            boolean isNewFile = !file.exists();

            FileWriter csvWriter = new FileWriter(file, true); // true для дописування в кінець файлу
            if (isNewFile) {
                csvWriter.append("id,name,volunteerInitiativeType,requiredAmount,coeffOfNecessity,publicationDate,deadlineDate,strategicImportance,impactOnUsers,deadline,economicEffect,externalFactors\n");
            }

            csvWriter.append(String.valueOf(initiative.getId()))
                    .append(",")
                    .append(initiative.getName())
                    .append(",")
                    .append(initiative.getVolunteerInitiativeType())
                    .append(",")
                    .append(String.valueOf(initiative.getRequiredAmount()))
                    .append(",")
                    .append(String.valueOf(initiative.getCoeffOfNecessity()))
                    .append(",")
                    .append(initiative.getPublicationDate().toString())
                    .append(",")
                    .append(initiative.getDeadlineDate().toString())
                    .append(",")
                    .append(String.valueOf(strategicImportance))
                    .append(",")
                    .append(String.valueOf(impactOnUsers))
                    .append(",")
                    .append(String.valueOf(deadline))
                    .append(",")
                    .append(String.valueOf(economicEffect))
                    .append(",")
                    .append(String.valueOf(externalFactors))
                    .append("\n");

            csvWriter.flush();
            csvWriter.close();
            System.out.println("CSV file updated successfully!");

        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private static final String CSV_FILE = "data.csv";

    public void writeListToCsv(List<VolunteerInitiative> initiatives) {
        try {
            File file = new File(CSV_FILE);
            boolean isNewFile = !file.exists();

            FileWriter csvWriter = new FileWriter(file, true); // true для дописування в кінець файлу
            if (isNewFile) {
                csvWriter.append("id,name,volunteerInitiativeType,requiredAmount,coeffOfNecessity,publicationDate,deadlineDate,strategicImportance,impactOnUsers,deadline,economicEffect,externalFactors\n");
            }

            for (VolunteerInitiative initiative : initiatives) {
                double strategicImportance = viTypeService.getCoeffOfNecessityByVolunteerInitiativeTypeName(
                        initiative.getVolunteerInitiativeType()
                );
                double impactOnUsers = initiative.getRequiredAmount().doubleValue() / 1000;
                double deadline = ChronoUnit.DAYS.between(initiative.getPublicationDate(), initiative.getDeadlineDate());

                double economicEffect = deadline != 0 ?
                        initiative.getRequiredAmount().doubleValue() / deadline :
                        0.0;

                double externalFactors = countInitiativesByType(initiatives, initiative.getVolunteerInitiativeType()) != 0 ?
                        (double) initiatives.size() / countInitiativesByType(initiatives, initiative.getVolunteerInitiativeType()) :
                        0.0;

                csvWriter.append(String.valueOf(initiative.getId()))
                        .append(",")
                        .append(initiative.getName())
                        .append(",")
                        .append(initiative.getVolunteerInitiativeType())
                        .append(",")
                        .append(String.valueOf(initiative.getRequiredAmount()))
                        .append(",")
                        .append(String.valueOf(initiative.getCoeffOfNecessity()))
                        .append(",")
                        .append(initiative.getPublicationDate().toString())
                        .append(",")
                        .append(initiative.getDeadlineDate().toString())
                        .append(",")
                        .append(String.valueOf(strategicImportance))
                        .append(",")
                        .append(String.valueOf(impactOnUsers))
                        .append(",")
                        .append(String.valueOf(deadline))
                        .append(",")
                        .append(String.valueOf(economicEffect))
                        .append(",")
                        .append(String.valueOf(externalFactors))
                        .append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("CSV file updated successfully!");

        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}

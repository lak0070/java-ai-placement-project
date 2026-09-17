package com.vityarthi.placement.service;

import com.vityarthi.placement.model.PredictionResult;
import com.vityarthi.placement.model.StudentProfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportService {
    public Path createReport(StudentProfile student, PredictionResult result, Path directory) throws IOException {
        Files.createDirectories(directory);
        String cleanName = student.name().trim().replaceAll("[^a-zA-Z0-9]+", "_");
        Path report = directory.resolve(cleanName + "_placement_report.txt");

        StringBuilder text = new StringBuilder()
                .append("PLACEMENT READINESS REPORT\n")
                .append("==========================\n")
                .append("Student: ").append(student.name()).append('\n')
                .append("Branch: ").append(student.branch()).append('\n')
                .append("Prediction: ").append(result.level().getDisplayName()).append('\n')
                .append(String.format("Neighbour agreement: %.0f%%%n", result.confidence() * 100))
                .append("\nRECOMMENDED ACTIONS\n");
        for (int i = 0; i < result.recommendations().size(); i++) {
            text.append(i + 1).append(". ").append(result.recommendations().get(i)).append('\n');
        }
        text.append("\nNote: This is an academic decision-support prototype, not a hiring decision.\n");
        Files.writeString(report, text.toString());
        return report;
    }
}

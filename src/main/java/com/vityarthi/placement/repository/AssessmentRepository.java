package com.vityarthi.placement.repository;

import com.vityarthi.placement.model.PredictionResult;
import com.vityarthi.placement.model.StudentProfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssessmentRepository {
    private static final String HEADER = "timestamp,name,branch,cgpa,dsa,aptitude,java,projects,communication,mock_interview,internships,prediction,confidence\n";
    private final Path file;

    public AssessmentRepository(Path file) {
        this.file = file;
    }

    public void save(StudentProfile student, PredictionResult result) throws IOException {
        if (file.getParent() != null) Files.createDirectories(file.getParent());
        if (Files.notExists(file)) Files.writeString(file, HEADER, StandardOpenOption.CREATE);

        String row = String.format("%s,%s,%s,%.2f,%d,%.2f,%.2f,%d,%.2f,%.2f,%d,%s,%.2f%n",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                safe(student.name()), safe(student.branch()), student.cgpa(), student.dsaProblems(),
                student.aptitudeScore(), student.javaScore(), student.projects(),
                student.communicationScore(), student.mockInterviewScore(), student.internships(),
                result.level(), result.confidence());
        Files.writeString(file, row, StandardOpenOption.APPEND);
    }

    private String safe(String value) {
        return value.replace(',', ' ').replace('\n', ' ').replace('\r', ' ');
    }
}

package com.vityarthi.placement.validation;

import com.vityarthi.placement.exception.ValidationException;
import com.vityarthi.placement.model.StudentProfile;

public final class InputValidator {
    private InputValidator() {
    }

    public static void validate(StudentProfile profile) {
        if (profile.name() == null || profile.name().isBlank()) {
            throw new ValidationException("Name cannot be empty.");
        }
        if (profile.branch() == null || profile.branch().isBlank()) {
            throw new ValidationException("Branch cannot be empty.");
        }
        range("CGPA", profile.cgpa(), 0, 10);
        range("DSA problems", profile.dsaProblems(), 0, 2000);
        range("Aptitude score", profile.aptitudeScore(), 0, 100);
        range("Java score", profile.javaScore(), 0, 100);
        range("Projects", profile.projects(), 0, 30);
        range("Communication score", profile.communicationScore(), 0, 100);
        range("Mock interview score", profile.mockInterviewScore(), 0, 100);
        range("Internships", profile.internships(), 0, 10);
    }

    private static void range(String field, double value, double min, double max) {
        if (Double.isNaN(value) || value < min || value > max) {
            throw new ValidationException(field + " must be between " + min + " and " + max + ".");
        }
    }
}

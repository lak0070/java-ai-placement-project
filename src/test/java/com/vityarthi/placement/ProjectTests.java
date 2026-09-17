package com.vityarthi.placement;

import com.vityarthi.placement.exception.ValidationException;
import com.vityarthi.placement.ml.CsvDataLoader;
import com.vityarthi.placement.ml.KnnClassifier;
import com.vityarthi.placement.model.ReadinessLevel;
import com.vityarthi.placement.model.StudentProfile;
import com.vityarthi.placement.validation.InputValidator;

import java.nio.file.Path;

public class ProjectTests {
    public static void main(String[] args) throws Exception {
        testStrongProfilePrediction();
        testInvalidCgpa();
        System.out.println("All validation tests passed.");
    }

    private static void testStrongProfilePrediction() throws Exception {
        var data = new CsvDataLoader().load(Path.of("data", "training_data.csv"));
        var result = new KnnClassifier(data, 5).classify(
                new StudentProfile("Test", "CSE", 9.1, 400, 88, 90, 4, 82, 86, 2).features());
        require(result.level() == ReadinessLevel.PLACEMENT_READY,
                "Strong profile should be classified as placement ready.");
    }

    private static void testInvalidCgpa() {
        try {
            InputValidator.validate(new StudentProfile("Test", "CSE", 12, 0, 0, 0, 0, 0, 0, 0));
            throw new AssertionError("Invalid CGPA was accepted.");
        } catch (ValidationException expected) {
            // Test passes.
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}

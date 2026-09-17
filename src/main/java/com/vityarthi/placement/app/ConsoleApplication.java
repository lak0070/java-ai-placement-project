package com.vityarthi.placement.app;

import com.vityarthi.placement.exception.ValidationException;
import com.vityarthi.placement.ml.CsvDataLoader;
import com.vityarthi.placement.ml.KnnClassifier;
import com.vityarthi.placement.model.PredictionResult;
import com.vityarthi.placement.model.StudentProfile;
import com.vityarthi.placement.repository.AssessmentRepository;
import com.vityarthi.placement.service.PredictionService;
import com.vityarthi.placement.service.RecommendationService;
import com.vityarthi.placement.service.ReportService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleApplication {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            var examples = new CsvDataLoader().load(Path.of("data", "training_data.csv"));
            var predictionService = new PredictionService(
                    new KnnClassifier(examples, 5), new RecommendationService());

            System.out.println("\nAI-BASED PLACEMENT READINESS ANALYZER");
            System.out.println("Enter values carefully. Scores must be from 0 to 100.\n");
            StudentProfile student = readStudent(scanner);
            PredictionResult result = predictionService.predict(student);

            printResult(result);
            new AssessmentRepository(Path.of("data", "assessment_history.csv")).save(student, result);
            Path report = new ReportService().createReport(student, result, Path.of("reports"));
            System.out.println("\nReport created: " + report.toAbsolutePath());
        } catch (ValidationException | NumberFormatException exception) {
            System.err.println("Invalid input: " + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("File error: " + exception.getMessage());
        }
    }

    private static StudentProfile readStudent(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Branch: ");
        String branch = scanner.nextLine().trim();
        double cgpa = readDouble(scanner, "CGPA (0-10): ");
        int dsa = readInt(scanner, "DSA problems solved: ");
        double aptitude = readDouble(scanner, "Aptitude score: ");
        double java = readDouble(scanner, "Java score: ");
        int projects = readInt(scanner, "Completed projects: ");
        double communication = readDouble(scanner, "Communication score: ");
        double mock = readDouble(scanner, "Mock interview score: ");
        int internships = readInt(scanner, "Internships completed: ");
        return new StudentProfile(name, branch, cgpa, dsa, aptitude, java,
                projects, communication, mock, internships);
    }

    private static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static void printResult(PredictionResult result) {
        System.out.println("\nRESULT");
        System.out.println("------");
        System.out.println("Readiness: " + result.level().getDisplayName());
        System.out.printf("Neighbour agreement: %.0f%%%n", result.confidence() * 100);
        System.out.println("\nRecommendations:");
        for (int i = 0; i < result.recommendations().size(); i++) {
            System.out.println((i + 1) + ". " + result.recommendations().get(i));
        }
    }
}

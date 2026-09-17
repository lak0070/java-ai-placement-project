package com.vityarthi.placement.ml;

import com.vityarthi.placement.model.ReadinessLevel;
import com.vityarthi.placement.model.TrainingExample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvDataLoader {
    public List<TrainingExample> load(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<TrainingExample> examples = new ArrayList<>();

        for (int lineNumber = 1; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber).trim();
            if (line.isEmpty()) continue;

            String[] columns = line.split(",");
            if (columns.length != 9) {
                throw new IOException("Invalid dataset row at line " + (lineNumber + 1));
            }

            try {
                List<Double> features = Arrays.stream(columns, 0, 8)
                        .map(String::trim)
                        .map(Double::parseDouble)
                        .toList();
                ReadinessLevel label = ReadinessLevel.valueOf(columns[8].trim());
                examples.add(new TrainingExample(features, label));
            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid value at dataset line " + (lineNumber + 1), exception);
            }
        }

        if (examples.size() < 3) {
            throw new IOException("Dataset must contain at least three examples.");
        }
        return examples;
    }
}

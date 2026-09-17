package com.vityarthi.placement.ml;

import com.vityarthi.placement.model.ReadinessLevel;
import com.vityarthi.placement.model.TrainingExample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class KnnClassifier {
    private final List<TrainingExample> trainingData;
    private final int k;
    private final double[] minimums;
    private final double[] maximums;

    public KnnClassifier(List<TrainingExample> trainingData, int k) {
        if (trainingData == null || trainingData.isEmpty()) {
            throw new IllegalArgumentException("Training data cannot be empty.");
        }
        if (k < 1 || k > trainingData.size()) {
            throw new IllegalArgumentException("k must be between 1 and the dataset size.");
        }
        this.trainingData = List.copyOf(trainingData);
        this.k = k;
        int featureCount = trainingData.get(0).features().size();
        this.minimums = new double[featureCount];
        this.maximums = new double[featureCount];
        calculateRanges();
    }

    public Classification classify(List<Double> input) {
        if (input.size() != minimums.length) {
            throw new IllegalArgumentException("Input has an incorrect number of features.");
        }

        List<Neighbour> neighbours = new ArrayList<>();
        for (TrainingExample example : trainingData) {
            neighbours.add(new Neighbour(example.label(), distance(input, example.features())));
        }
        neighbours.sort(Comparator.comparingDouble(Neighbour::distance));

        Map<ReadinessLevel, Integer> votes = new EnumMap<>(ReadinessLevel.class);
        Map<ReadinessLevel, Double> distanceTotals = new EnumMap<>(ReadinessLevel.class);
        for (int i = 0; i < k; i++) {
            Neighbour neighbour = neighbours.get(i);
            votes.merge(neighbour.label(), 1, Integer::sum);
            distanceTotals.merge(neighbour.label(), neighbour.distance(), Double::sum);
        }

        ReadinessLevel winner = votes.keySet().stream()
                .max(Comparator
                        .comparingInt((ReadinessLevel level) -> votes.get(level))
                        .thenComparingDouble(level -> -distanceTotals.get(level)))
                .orElseThrow();
        return new Classification(winner, votes.get(winner) / (double) k);
    }

    private void calculateRanges() {
        for (int feature = 0; feature < minimums.length; feature++) {
            minimums[feature] = Double.POSITIVE_INFINITY;
            maximums[feature] = Double.NEGATIVE_INFINITY;
            for (TrainingExample example : trainingData) {
                double value = example.features().get(feature);
                minimums[feature] = Math.min(minimums[feature], value);
                maximums[feature] = Math.max(maximums[feature], value);
            }
        }
    }

    private double distance(List<Double> first, List<Double> second) {
        double sum = 0;
        for (int i = 0; i < first.size(); i++) {
            double range = maximums[i] - minimums[i];
            double difference = range == 0 ? 0 : (first.get(i) - second.get(i)) / range;
            sum += difference * difference;
        }
        return Math.sqrt(sum);
    }

    public record Classification(ReadinessLevel level, double confidence) {
    }

    private record Neighbour(ReadinessLevel label, double distance) {
    }
}

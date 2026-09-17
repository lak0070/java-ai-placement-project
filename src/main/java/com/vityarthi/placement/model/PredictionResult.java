package com.vityarthi.placement.model;

import java.util.List;

public record PredictionResult(
        ReadinessLevel level,
        double confidence,
        List<String> recommendations) {
}

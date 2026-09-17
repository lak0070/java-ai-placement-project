package com.vityarthi.placement.model;

import java.util.List;

public record TrainingExample(List<Double> features, ReadinessLevel label) {
}

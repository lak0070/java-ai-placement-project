package com.vityarthi.placement.service;

import com.vityarthi.placement.ml.KnnClassifier;
import com.vityarthi.placement.model.PredictionResult;
import com.vityarthi.placement.model.StudentProfile;
import com.vityarthi.placement.validation.InputValidator;

public class PredictionService {
    private final KnnClassifier classifier;
    private final RecommendationService recommendationService;

    public PredictionService(KnnClassifier classifier, RecommendationService recommendationService) {
        this.classifier = classifier;
        this.recommendationService = recommendationService;
    }

    public PredictionResult predict(StudentProfile student) {
        InputValidator.validate(student);
        KnnClassifier.Classification classification = classifier.classify(student.features());
        return new PredictionResult(
                classification.level(),
                classification.confidence(),
                recommendationService.generate(student));
    }
}

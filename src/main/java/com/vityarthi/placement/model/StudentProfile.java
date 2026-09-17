package com.vityarthi.placement.model;

import java.util.List;

public record StudentProfile(
        String name,
        String branch,
        double cgpa,
        int dsaProblems,
        double aptitudeScore,
        double javaScore,
        int projects,
        double communicationScore,
        double mockInterviewScore,
        int internships) {

    public List<Double> features() {
        return List.of(
                cgpa, (double) dsaProblems, aptitudeScore, javaScore,
                (double) projects, communicationScore,
                mockInterviewScore, (double) internships);
    }
}

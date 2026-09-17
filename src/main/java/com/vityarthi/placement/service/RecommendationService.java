package com.vityarthi.placement.service;

import com.vityarthi.placement.model.StudentProfile;

import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    public List<String> generate(StudentProfile student) {
        List<String> advice = new ArrayList<>();
        if (student.dsaProblems() < 100) advice.add("Solve at least 100 DSA problems, starting with arrays and strings.");
        if (student.javaScore() < 70) advice.add("Revise Java OOP, collections, exception handling and multithreading.");
        if (student.aptitudeScore() < 65) advice.add("Practise quantitative aptitude for 30 minutes each day.");
        if (student.projects() < 2) advice.add("Complete at least two original projects and publish them on GitHub.");
        if (student.communicationScore() < 65) advice.add("Practise a two-minute self-introduction and explain one project aloud.");
        if (student.mockInterviewScore() < 65) advice.add("Take two mock interviews and record the feedback.");
        if (student.internships() == 0) advice.add("Apply for internships or contribute to an open-source project.");
        if (student.cgpa() < 7.0) advice.add("Check company eligibility criteria and strengthen your technical portfolio.");
        if (advice.isEmpty()) advice.add("Maintain consistency and start applying to suitable placement opportunities.");
        return List.copyOf(advice);
    }
}

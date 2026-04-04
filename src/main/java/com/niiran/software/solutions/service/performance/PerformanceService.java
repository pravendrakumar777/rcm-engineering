package com.niiran.software.solutions.service.performance;

import com.niiran.software.solutions.domain.performance.PerformanceReview;

import java.util.List;

public interface PerformanceService {

    List<PerformanceReview> getReviewsByEmployee(String ohr);
    PerformanceReview saveReview(PerformanceReview review);
    void deleteReview(Long id);
}

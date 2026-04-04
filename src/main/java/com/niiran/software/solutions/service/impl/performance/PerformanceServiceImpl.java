package com.niiran.software.solutions.service.impl.performance;

import com.niiran.software.solutions.domain.performance.PerformanceReview;
import com.niiran.software.solutions.repository.performance.PerformanceReviewRepository;
import com.niiran.software.solutions.service.performance.PerformanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceReviewRepository performanceReviewRepository;

    public PerformanceServiceImpl(PerformanceReviewRepository performanceReviewRepository) {
        this.performanceReviewRepository = performanceReviewRepository;
    }

    @Override
    public List<PerformanceReview> getReviewsByEmployee(String ohr) {
        return performanceReviewRepository.findByEmployeeOhr(ohr);
    }

    @Override
    public PerformanceReview saveReview(PerformanceReview review) {
        return performanceReviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        performanceReviewRepository.deleteById(id);
    }
}

package com.niiran.software.solutions.repository.performance;

import com.niiran.software.solutions.domain.performance.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByEmployeeOhr(String ohr);
}

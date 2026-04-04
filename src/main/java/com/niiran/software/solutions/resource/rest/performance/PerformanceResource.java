package com.niiran.software.solutions.resource.rest.performance;

import com.niiran.software.solutions.domain.performance.PerformanceReview;
import com.niiran.software.solutions.service.performance.PerformanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceResource {
    private final PerformanceService performanceService;

    public PerformanceResource(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/{ohr}")
    public List<PerformanceReview> getReviews(@PathVariable String ohr) {
        return performanceService.getReviewsByEmployee(ohr);
    }

    @PostMapping
    public PerformanceReview addReview(@RequestBody PerformanceReview review) {
        return performanceService.saveReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        performanceService.deleteReview(id);
    }
}

package com.niiran.software.solutions.resource.performance;

import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.performance.PerformanceReview;
import com.niiran.software.solutions.repository.EmployeeRepository;
import com.niiran.software.solutions.service.EmployeeService;
import com.niiran.software.solutions.service.performance.PerformanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/performances")
public class PerformanceController {

    private final EmployeeService employeeService;
    private final PerformanceService performanceService;
    private final EmployeeRepository employeeRepository;

    public PerformanceController(EmployeeService employeeService, PerformanceService performanceService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.performanceService = performanceService;
        this.employeeRepository = employeeRepository;
    }

    // Show performance reviews for an employee by OHR
    // Management landing page: list all employees with performance summary
    @GetMapping
    public String managePerformance(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "performance_manage"; // maps to performance_manage.ftl
    }

    // Show performance reviews for an employee by OHR
    @GetMapping("/{ohr}/performance")
    public String showPerformance(@PathVariable String ohr, Model model) {
        Employee emp = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with OHR: " + ohr));
        model.addAttribute("employee", emp);
        model.addAttribute("reviews", performanceService.getReviewsByEmployee(ohr));
        return "performance"; // maps to performance.ftl
    }

    @GetMapping("/{ohr}/performance/new")
    public String newReview(@PathVariable String ohr, Model model) {
        Employee emp = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with OHR: " + ohr));
        PerformanceReview review = new PerformanceReview();
        review.setEmployee(emp);
        review.setReviewDate(LocalDate.now());
        model.addAttribute("review", review);
        return "performance_form"; // maps to performance_form.ftl
    }

    @PostMapping("/{ohr}/performance")
    public String saveReview(@PathVariable String ohr, @ModelAttribute PerformanceReview review) {
        Employee emp = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with OHR: " + ohr));
        review.setEmployee(emp);
        review.setReviewDate(LocalDate.now());
        performanceService.saveReview(review);
        return "redirect:/performances/" + ohr + "/performance";
    }
}

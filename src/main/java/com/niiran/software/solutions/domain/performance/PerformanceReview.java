package com.niiran.software.solutions.domain.performance;

import com.niiran.software.solutions.domain.Employee;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "rcm_performance_review")
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reviewer;
    private String comments;
    private int rating;
    private LocalDate reviewDate;
    @ManyToOne
    @JoinColumn(name = "employee_ohr", referencedColumnName = "ohr", nullable = false)
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceReview that = (PerformanceReview) o;
        return rating == that.rating && Objects.equals(id, that.id) && Objects.equals(reviewer, that.reviewer) && Objects.equals(comments, that.comments) && Objects.equals(reviewDate, that.reviewDate) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewer, comments, rating, reviewDate, employee);
    }

    @Override
    public String toString() {
        return "PerformanceReview{" +
                "id=" + id +
                ", reviewer='" + reviewer + '\'' +
                ", comments='" + comments + '\'' +
                ", rating=" + rating +
                ", reviewDate=" + reviewDate +
                ", employee=" + employee +
                '}';
    }
}

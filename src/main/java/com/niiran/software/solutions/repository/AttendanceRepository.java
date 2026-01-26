package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.Attendance;
import com.niiran.software.solutions.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Attendance> findByEmployee_Ohr(String ohr);
    List<Attendance> findByEmployeeOhrAndDateBetween(String ohr, LocalDate fromDate, LocalDate toDate);
}

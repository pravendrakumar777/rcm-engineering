package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.ohr = :ohr")
    Optional<Employee> findByOhr(@Param("ohr") String ohr);

    List<Employee> findByStatus(EmployeeStatus status);

    @Query("SELECT e FROM Employee e WHERE UPPER(e.status) = 'ACTIVE'")
    List<Employee> findAllActiveEmployees();

    Optional<Employee> findByNameIgnoreCaseOrOhrIgnoreCase(String name, String ohr);
}

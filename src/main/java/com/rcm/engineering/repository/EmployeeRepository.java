package com.rcm.engineering.repository;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.empCode = :empCode")
    Optional<Employee> findByEmpCode(@Param("empCode") String empCode);

    List<Employee> findByStatus(EmployeeStatus status);

    @Query("SELECT e FROM Employee e WHERE UPPER(e.status) = 'ACTIVE'")
    List<Employee> findAllActiveEmployees();

    Optional<Employee> findByNameIgnoreCaseOrEmpCodeIgnoreCase(String name, String empCode);
}

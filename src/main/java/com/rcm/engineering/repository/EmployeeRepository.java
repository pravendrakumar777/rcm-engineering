package com.rcm.engineering.repository;

import com.rcm.engineering.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    @Query("SELECT e FROM Employee e WHERE e.empCode = :empCode")
//    Optional<Employee> findByEmpCode(String empCode);

    @Query("SELECT e FROM Employee e WHERE e.empCode = :empCode")
    Optional<Employee> findByEmpCode(@Param("empCode") String empCode);

}

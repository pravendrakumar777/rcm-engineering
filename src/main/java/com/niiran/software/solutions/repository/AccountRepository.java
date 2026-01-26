package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmployeeId(Long employeeId);
}

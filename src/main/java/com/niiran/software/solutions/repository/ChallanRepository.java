package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.Challan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallanRepository extends JpaRepository<Challan, Long> {
    boolean existsByRefChNo(String refChNo);
    Challan getByChallanNo(String challanNo);
}

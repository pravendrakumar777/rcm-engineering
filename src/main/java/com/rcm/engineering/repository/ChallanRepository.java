package com.rcm.engineering.repository;

import com.rcm.engineering.domain.Challan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallanRepository extends JpaRepository<Challan, Long> {
    boolean existsByRefChNo(String refChNo);
    Challan getByChallanNo(String challanNo);
}

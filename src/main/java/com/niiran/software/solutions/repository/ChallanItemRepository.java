package com.niiran.software.solutions.repository;

import com.niiran.software.solutions.domain.ChallanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallanItemRepository extends JpaRepository<ChallanItem, Long> {
}

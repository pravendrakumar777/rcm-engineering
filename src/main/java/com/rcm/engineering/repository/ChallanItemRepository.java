package com.rcm.engineering.repository;

import com.rcm.engineering.domain.ChallanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallanItemRepository extends JpaRepository<ChallanItem, Long> {
}

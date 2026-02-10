package com.niiran.software.solutions.repository.org;

import com.niiran.software.solutions.domain.org.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByOrganizationId(String organizationId);
    @Query(value = "SELECT * FROM organizations ORDER BY created_date DESC", nativeQuery = true)
    List<Organization> findAllByOrderByCreatedDateDescNative();
}

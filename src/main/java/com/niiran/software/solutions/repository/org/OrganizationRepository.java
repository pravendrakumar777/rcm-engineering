package com.niiran.software.solutions.repository.org;

import com.niiran.software.solutions.domain.org.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByOrganizationId(String organizationId);
}

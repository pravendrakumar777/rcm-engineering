package com.niiran.software.solutions.service.impl.org;

import com.niiran.software.solutions.domain.org.Organization;
import com.niiran.software.solutions.exceptions.ResourceNotFoundException;
import com.niiran.software.solutions.repository.org.OrganizationRepository;
import com.niiran.software.solutions.service.org.OrganizationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization save(Organization org) {
        if (org.getOrganizationId() == null || org.getOrganizationId().isEmpty()) {
            org.setOrganizationId(generateOrganizationId(org.getOrganizationName()));
        }
        org.setCreatedDate(LocalDateTime.now());
        return organizationRepository.save(org);
    }

    @Override
    public List<Organization> getAll() {
        return organizationRepository.findAllByOrderByCreatedDateDescNative();
    }

    @Override
    public Organization getById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }

    private String generateOrganizationId(String organizationName) {
        String cleanName = organizationName
                .trim()
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase();

        if (cleanName.length() < 3) {
            throw new RuntimeException("Company name must have at least 3 alphabet characters");
        }
        String prefix = cleanName.substring(0, 3);
        String orgId;
        Random random = new Random();
        do {
            int number = 100000 + random.nextInt(900000);
            orgId = prefix + number;
        } while (organizationRepository.existsByOrganizationId(orgId));
        return orgId;
    }
}

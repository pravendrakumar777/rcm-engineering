package com.niiran.software.solutions.service.org;

import com.niiran.software.solutions.domain.org.Organization;

import java.util.List;

public interface OrganizationService {

    Organization save(Organization org);
    List<Organization> getAll();
    Organization getById(Long id);
    void delete(Long id);
}

package com.niiran.software.solutions.resource.rest.org;

import com.niiran.software.solutions.domain.org.Organization;
import com.niiran.software.solutions.service.org.OrganizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin("*")
public class OrganizationResource {
    private final OrganizationService organizationService;
    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    @PostMapping("/create")
    public Organization create(@RequestBody Organization org) {
        return organizationService.save(org);
    }

    @GetMapping("/list")
    public List<Organization> list() {
        return organizationService.getAll();
    }

    @GetMapping("/fetch/{id}")
    public Organization fetch(@PathVariable Long id) {
        return organizationService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        organizationService.delete(id);
    }
}

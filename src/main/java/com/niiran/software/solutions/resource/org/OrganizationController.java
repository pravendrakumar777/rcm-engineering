package com.niiran.software.solutions.resource.org;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrganizationController {
    @GetMapping("/organizations")
    public String organizations() {
        return "organizations-list";
    }

    @GetMapping("/organizations/create")
    public String createOrganization() {
        return "organization-create";
    }
}

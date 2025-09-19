package com.rcm.engineering.service;

import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.FtlToPdfUtil;
import freemarker.template.Configuration;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final Configuration freemarkerConfig;
    private final FtlToPdfUtil ftlToPdfUtil;

    public EmployeeService(Configuration freemarkerConfig, FtlToPdfUtil ftlToPdfUtil) {
        this.freemarkerConfig = freemarkerConfig;
        this.ftlToPdfUtil = ftlToPdfUtil;
    }

    public byte[] generateEmployeeProfilePdf(String empCode) {
        return ftlToPdfUtil.generateEmployeeProfile(empCode, freemarkerConfig);
    }
}

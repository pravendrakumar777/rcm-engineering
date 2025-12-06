package com.rcm.engineering.service.impl;

import com.rcm.engineering.config.BindingChannel;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.domain.events.EmployeeEvent;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.FtlToPdfUtil;
import com.rcm.engineering.service.EmployeeService;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final Configuration freemarkerConfig;
    private final FtlToPdfUtil ftlToPdfUtil;
    private final EmployeeRepository employeeRepository;
    private final BindingChannel bindingChannel;

    public EmployeeServiceImpl(Configuration freemarkerConfig, FtlToPdfUtil ftlToPdfUtil, EmployeeRepository employeeRepository, BindingChannel bindingChannel) {
        this.freemarkerConfig = freemarkerConfig;
        this.ftlToPdfUtil = ftlToPdfUtil;
        this.employeeRepository = employeeRepository;
        this.bindingChannel = bindingChannel;
    }

    public byte[] generateEmployeeProfilePdf(String empCode) {
        return ftlToPdfUtil.generateEmployeeProfile(empCode, freemarkerConfig);
    }


    @Override
    public Employee createEmployee(Employee employee) {
        log.info("Service Request to createEmployee: {}", employee);
        employee.setStatus(EmployeeStatus.PENDING);
        Employee saveEmp = employeeRepository.save(employee);
        // Pre event for Cm
        EmployeeEvent event = new EmployeeEvent(
                saveEmp.getEmpCode(),
                saveEmp.getName(),
                saveEmp.getMobile(),
                saveEmp.getGender(),
                saveEmp.getEmail(),
                saveEmp.getManager(),
                saveEmp.getDateOfBirth(),
                saveEmp.getAddress(),
                saveEmp.getCity(),
                saveEmp.getState(),
                saveEmp.getPostalCode(),
                saveEmp.getCountry(),
                saveEmp.getDepartment(),
                saveEmp.getDesignation(),
                saveEmp.getDateOfJoining(),
                saveEmp.getPanNumber(),
                saveEmp.getAadhaarNumber(),
                saveEmp.getBankName(),
                saveEmp.getBankAccountNumber(),
                saveEmp.getIfscCode(),
                saveEmp.getSalary(),
                saveEmp.getStatus()
        );
        try {
            MessageChannel preOnboardingMessageChannel = bindingChannel.preOnboardingRequestOutput();
            boolean sent = preOnboardingMessageChannel.send(MessageBuilder.withPayload(event).build());
            if (!sent) {
                throw new RuntimeException("KAFKA PUBLISH FAILED FOR PRE-ONBOARDING: " + event.getEmpCode());
            }
            log.info("PRE-ONBOARDING EVENT PUBLISHED SUCCESSFULLY: {}", event);
        } catch (Exception ex) {
            log.error("Error publishing pre-onboarding event to Kafka: {}", ex.getMessage(), ex);
            throw ex;
        }
        return saveEmp;
    }

    @Override
    public void updateStatusFromCM(EmployeeEvent event) {
        log.info("Updating employee status from CM approval response updateStatusFromCM: {}", event);
        Employee employee = employeeRepository.findByEmpCode(event.getEmpCode())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getStatus() == EmployeeStatus.APPROVED) {
            employee.setStatus(EmployeeStatus.ACTIVE);
        } else {
            employee.setStatus(EmployeeStatus.REJECTED);
        }
        employeeRepository.save(employee);
        log.info("Employee status updated to {}", employee.getStatus());
    }

    @Override
    public void updateStatus(String empCode, EmployeeStatus newStatus) {
        Employee emp = employeeRepository.findByEmpCode(empCode).orElse(null);
        if (emp != null) {
            emp.setStatus(newStatus);
            employeeRepository.save(emp);
        }
    }
}

package com.niiran.software.solutions.service.impl;

import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;
import com.niiran.software.solutions.exceptions.EmployeeCreationException;
import com.niiran.software.solutions.repository.EmployeeRepository;
import com.niiran.software.solutions.resource.utils.FtlToPdfUtil;
import com.niiran.software.solutions.service.EmployeeService;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final Configuration freemarkerConfig;
    private final FtlToPdfUtil ftlToPdfUtil;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(Configuration freemarkerConfig, FtlToPdfUtil ftlToPdfUtil, EmployeeRepository employeeRepository) {
        this.freemarkerConfig = freemarkerConfig;
        this.ftlToPdfUtil = ftlToPdfUtil;
        this.employeeRepository = employeeRepository;
    }

    public byte[] generateEmployeeProfilePdf(String ohr) {
        return ftlToPdfUtil.generateEmployeeProfile(ohr, freemarkerConfig);
    }

    @Override
    public Employee createEmployee(Employee employee) {

        String traceId = MDC.get("traceId");
        final String endpoint = "/employees/create";
        try {

            log.info("traceId: {} | Source: APK | RequestType: REST | Endpoint: {} | Action: createEmployee | Step: START | Payload: {}",
                    traceId, endpoint, employee);

            // Generate OHR Code
            String ohrCode = "NII" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));

            employee.setOhr(ohrCode);

            log.debug("traceId: {} | Endpoint: {} | Step: GENERATE_OHR | OHR: {}",
                    traceId, endpoint, ohrCode);


            // Validate OHR
            if (employee.getOhr() == null || employee.getOhr().trim().isEmpty()) {
                log.error("traceId: {} | Endpoint: {} | Step: VALIDATE_OHR | Result: FAILED | Reason: OHR_EMPTY",
                        traceId, endpoint);
                throw new EmployeeCreationException("OHR cannot be empty");
            }

            // Check Duplicate OHR
            boolean exists = employeeRepository.existsByOhr(employee.getOhr());

            if (exists) {

                log.error("traceId: {} | Endpoint: {} | Step: CHECK_DUPLICATE_OHR | Result: FAILED | OHR_ALREADY_EXISTS: {}",
                        traceId, endpoint, employee.getOhr());

                throw new EmployeeCreationException("Employee with this OHR already exists");
            }

            // Set Status
            employee.setStatus(EmployeeStatus.PENDING);

            log.debug("traceId: {} | Endpoint: {} | Step: SET_STATUS | Status: {}",
                    traceId, endpoint, employee.getStatus());

            // Save Employee
            Employee savedEmployee = employeeRepository.save(employee);

            log.info("traceId: {} | Endpoint: {} | Step: REPOSITORY_SAVE | Result: SUCCESS | EmployeeId: {}",
                    traceId, endpoint, savedEmployee.getId());

            log.info("traceId: {} | Endpoint: {} | Step: END | Status: RETURN_SUCCESS", traceId, endpoint);
            return savedEmployee;

        } catch (DataAccessException dae) {

            log.error("traceId: {} | Endpoint: {} | Step: ERROR | Type: Database | Message: {} | Payload: {}",
                    traceId, endpoint, dae.getMessage(), employee, dae);

            throw new EmployeeCreationException("Database error while creating employee", dae);

        } catch (Exception ex) {

            log.error("traceId: {} | Endpoint: {} | Step: ERROR | Type: Exception | Message: {} | Payload: {}",
                    traceId, endpoint, ex.getMessage(), employee, ex);

            throw new EmployeeCreationException("Unexpected error while creating employee", ex);
        }
    }

    @Override
    public void updateStatus(String ohr, EmployeeStatus newStatus) {
        Employee emp = employeeRepository.findByOhr(ohr).orElse(null);
        if (emp != null) {
            if(emp.getStatus() == EmployeeStatus.PENDING) {
                emp.setStatus(newStatus);
                employeeRepository.save(emp);
            }
        }
    }

    @Override
    public List<Employee> getAllActive() {
        return employeeRepository.findAllActiveEmployees();
    }
}

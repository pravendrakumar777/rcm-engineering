package com.rcm.engineering.cm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.dto.EmployeeEventDTO;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class ConsentManager {
    private static final Logger log = LoggerFactory.getLogger(ConsentManager.class);
    private final EmployeeRepository employeeRepository;
    private static final DateTimeFormatter DMY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public ConsentManager(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //@ServiceActivator(inputChannel = BindingChannel.PRE_ONBOARDING_REQUEST_INPUT)
    //@Transactional
    public void preOnboardingRequestSubscriber(byte[] payload) {
        try {
            log.info("📥 CM RECEIVED PRE-ONBOARDING REQUEST RAW :: {}", new String(payload));
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            EmployeeEventDTO dto = mapper.readValue(payload, EmployeeEventDTO.class);
            // CM internal approval decision
            boolean approve = true;
            EmployeeStatus newStatus = approve ? EmployeeStatus.APPROVED : EmployeeStatus.CANCEL;
            Optional<Employee> existingOpt = employeeRepository.findByEmpCode(dto.getEmpCode());

            if (existingOpt.isPresent()) {
                Employee emp = existingOpt.get();
                log.info("🔎 EMPLOYEE FOUND {} with status {}", emp.getEmpCode(), emp.getStatus());

                if (emp.getStatus() == EmployeeStatus.PENDING) {
                    emp.setStatus(newStatus);
                    //employeeRepository.save(emp);
                    log.info("🟢 STATUS UPDATED to {}", newStatus);
                } else {
                    log.warn("⚠ SKIPPED — Already {}. No overwrite.", emp.getStatus());
                }
                return;
            }

            Employee emp = new Employee();
            BeanUtils.copyProperties(dto, emp);
            emp.setDateOfBirth(parseDate(dto.getDateOfBirth()));
            emp.setDateOfJoining(parseDate(dto.getDateOfJoining()));
            emp.setStatus(newStatus);
            employeeRepository.save(emp);
            log.info("🆕 NEW EMPLOYEE CREATED with status {}", newStatus);
        } catch (Exception e) {
            log.error("❌ Error while processing PRE-ONBOARDING REQUEST", e);
        }
    }
    private LocalDate parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(date, DMY_FORMATTER);
        } catch (Exception ex) {
            log.error("⚠️ Invalid date received: {}", date);
            return null;
        }
    }
}

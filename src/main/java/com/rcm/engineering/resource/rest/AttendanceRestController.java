package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRestController {
    private static final Logger log = LoggerFactory.getLogger(AttendanceRestController.class);
    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceRestController(AttendanceService attendanceService, EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> attendanceDashboardData() {
        Map<String, Object> response = new HashMap<>();

        LocalDate today = LocalDate.now();
        List<Attendance> allAttendance = attendanceService.getAllAttendance(today, today);
        List<Attendance> presentEmployees = allAttendance.stream()
                .filter(att -> Attendance.Status.PRESENT.equals(att.getStatus()))
                .collect(Collectors.toList());

        List<Attendance> absentEmployees = allAttendance.stream()
                .filter(att -> Attendance.Status.ABSENT.equals(att.getStatus()))
                .collect(Collectors.toList());

        long totalEmployees = allAttendance.size();
        long presentCount = presentEmployees.size();
        long absentCount = absentEmployees.size();

        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        List<Employee> pendingEmployees = employeeRepository.findByStatus(EmployeeStatus.PENDING);
        List<Employee> cancelEmployees = employeeRepository.findByStatus(EmployeeStatus.CANCEL);

        response.put("today", today);
        response.put("totalEmployees", totalEmployees);
        response.put("presentCount", presentCount);
        response.put("absentCount", absentCount);
        response.put("allAttendance", allAttendance);
        response.put("presentEmployees", presentEmployees);
        response.put("absentEmployees", absentEmployees);
        response.put("activeEmployees", activeEmployees);
        response.put("pendingEmployees", pendingEmployees);
        response.put("cancelEmployees", cancelEmployees);
        return response;
    }

    @GetMapping("/{empCode}")
    public ResponseEntity<List<Attendance>> getAttendanceByEmpCode(@PathVariable String empCode) {
        log.info("REST Request to getAttendanceByEmpCode: {}", empCode);
        List<Attendance> attendances = attendanceService.getAttendanceByEmpCode(empCode);
        if (attendances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendances);
    }
}

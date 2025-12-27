package com.rcm.engineering.resource.rest;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.ExportUtils;
import com.rcm.engineering.service.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
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

    @GetMapping("/{empCode}")
    public ResponseEntity<List<Attendance>> getAttendanceByEmpCode(@PathVariable String empCode) {
        log.info("REST Request to getAttendanceByEmpCode: {}", empCode);
        List<Attendance> attendances = attendanceService.getAttendanceByEmpCode(empCode);
        if (attendances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendances);
    }


    @PostMapping("/mark/{empCode}")
    public ResponseEntity<?> markAttendance(
            @RequestParam String empCode,
            @RequestParam String date,
            @RequestParam Attendance.Status status,
            @RequestParam(required = false) String checkInDateTime,
            @RequestParam(required = false) String checkOutDateTime) {

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Invalid date format. Expected yyyy-MM-dd"));
        }

        LocalDate today = LocalDate.now();
        if (!parsedDate.equals(today)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Attendance can only be marked for today"));
        }

        LocalDateTime checkIn = parseDateTime(checkInDateTime);
        LocalDateTime checkOut = parseDateTime(checkOutDateTime);

        try {
            Attendance updated = attendanceService.markAttendance(empCode, parsedDate, status, checkIn, checkOut);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", ex.getMessage()));
        }
    }
    private LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected yyyy-MM-ddTHH:mm:ss");
        }
    }

    @GetMapping("/export/csv")
    public void exportCsv(@RequestParam String empCode,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                          HttpServletResponse response) throws IOException {
        log.info("CSV export requested for empCode={} from {} to {}", empCode, fromDate, toDate);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance-csv-" + empCode + ".csv");

        List<Attendance> records = attendanceService.getAttendanceForEmployeeBetween(empCode, fromDate, toDate);
        log.debug("Fetched {} attendance records for empCode={}", records.size(), empCode);

        try {
            ExportUtils.writeAttendanceCSV(records, response.getOutputStream());
            log.info("CSV export completed successfully for empCode={}", empCode);
        } catch (Exception e) {
            log.error("Error during CSV export for empCode={}", empCode, e);
            throw e;
        }
    }

    @GetMapping("/export/excel")
    public void exportExcel(@RequestParam String empCode,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                            HttpServletResponse response) throws IOException {
        log.info("Excel export requested for empCode={} from {} to {}", empCode, fromDate, toDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=attendance-excel-" + empCode + ".xlsx");

        List<Attendance> records = attendanceService.getAttendanceForEmployeeBetween(empCode, fromDate, toDate);
        log.debug("Fetched {} attendance records for empCode={}", records.size(), empCode);

        try {
            ExportUtils.writeAttendanceExcel(records, response.getOutputStream());
            log.info("Excel export completed successfully for empCode={}", empCode);
        } catch (Exception e) {
            log.error("Error during Excel export for empCode={}", empCode, e);
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public Map<String, Object> attendanceDashboard() {
        log.info("REST Request to getAttendanceDashboard : {}");
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
}
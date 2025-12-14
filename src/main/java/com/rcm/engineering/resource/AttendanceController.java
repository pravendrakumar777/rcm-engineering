package com.rcm.engineering.resource;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(AttendanceService attendanceService, EmployeeRepository employeeRepository) {
        this.attendanceService = attendanceService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String attendancePage(Model model) {
        return "attendance";
    }

    @PostMapping("/api/mark")
    @ResponseBody
    public ResponseEntity<?> markAttendance(
            @RequestParam String empCode,
            @RequestParam String date,
            @RequestParam Attendance.Status status,
            @RequestParam(required = false) String checkInDateTime,
            @RequestParam(required = false) String checkOutDateTime) {

        LocalDate parsedDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        if (!parsedDate.equals(today)) {
            return ResponseEntity.badRequest().body(null);
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

    private LocalDateTime parseDateTime(String input) {
        if (input == null || input.trim().isEmpty()) return null;

        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e1) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e2) {
                throw new RuntimeException("Invalid date-time format: " + input);
            }
        }
    }


    @GetMapping("/api/{empCode}/month")
    @ResponseBody
    public ResponseEntity<List<Attendance>> getMonthlyAttendance(
            @PathVariable String empCode,
            @RequestParam String start,
            @RequestParam String end) {

        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        List<Attendance> attendanceList = attendanceService.getAttendance(empCode, s, e)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/report")
    public String getMonthlyAttendanceView(
            @RequestParam(required = false) String empCode,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Model model) {

        if (empCode != null && start != null && end != null) {
            try {
                LocalDate s = LocalDate.parse(start);
                LocalDate e = LocalDate.parse(end);
                List<Attendance> attendanceList = attendanceService.getAttendance(empCode, s, e)
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                Duration totalDuration = attendanceList.stream()
                        .filter(att -> att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null)
                        .map(att -> Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime()))
                        .reduce(Duration.ZERO, Duration::plus);

                long totalMinutes = totalDuration.toMinutes();
                long hours = totalMinutes / 60;
                long minutes = totalMinutes % 60;

                long presentCount = attendanceList.stream()
                        .filter(att -> "PRESENT".equalsIgnoreCase(att.getStatus().toString()))
                        .count();
                long absentCount = attendanceList.stream()
                        .filter(att -> "ABSENT".equalsIgnoreCase(att.getStatus().toString()))
                        .count();

                String statusSummary = String.format("P-%ddays, A-%ddays", presentCount, absentCount);
                model.addAttribute("presentCount", presentCount);
                model.addAttribute("absentCount", absentCount);
                model.addAttribute("statusSummary", statusSummary);
                long totalDays = presentCount + absentCount;
                model.addAttribute("totalDays", totalDays);

                String formattedTotal = String.format("%d Hrs %d Mins", hours, minutes);
                model.addAttribute("totalWorkingHours", formattedTotal);

                model.addAttribute("attendanceList", attendanceList);
            } catch (Exception ex) {
                model.addAttribute("errorMessage", "Employee not found or invalid data.");
            }
        }
        return "attendance-report";
    }

    @GetMapping("/dashboard")
    public String attendanceDashboard(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("USER");
            model.addAttribute("currentRole", role);
        }

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

        model.addAttribute("today", today);
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        model.addAttribute("allAttendance", allAttendance);
        model.addAttribute("presentEmployees", presentEmployees);
        model.addAttribute("absentEmployees", absentEmployees);

        model.addAttribute("activeEmployees", activeEmployees);
        model.addAttribute("pendingEmployees", pendingEmployees);
        model.addAttribute("cancelEmployees", cancelEmployees);
        return "attendance-dashboard";
    }
}

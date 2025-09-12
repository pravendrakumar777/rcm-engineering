package com.rcm.engineering.resource;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.dto.AttendanceDTO;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.PdfGeneratorUtil;
import com.rcm.engineering.service.AttendanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    // ---------------- REST ENDPOINTS ----------------


    @PostMapping("/api/mark")
    @ResponseBody
    public ResponseEntity<Attendance> markAttendance(
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

        Attendance updated = attendanceService.markAttendance(empCode, parsedDate, status, checkIn, checkOut);
        return ResponseEntity.ok(updated);
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
}

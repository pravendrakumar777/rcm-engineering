package com.rcm.engineering.resource;

import com.itextpdf.io.exceptions.IOException;
import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.resource.utils.PdfGeneratorUtil;
import com.rcm.engineering.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;

    public EmployeeController(EmployeeRepository employeeRepository, AttendanceService attendanceService) {
        this.employeeRepository = employeeRepository;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee-list";
    }

    @GetMapping("/form")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("formTitle", "Create New Employee");
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        if (employee.getEmpCode() != null && !employee.getEmpCode().isEmpty()) {
            Optional<Employee> existingEmployee = employeeRepository.findByEmpCode(employee.getEmpCode());
            if (existingEmployee.isPresent()) {
                Employee existing = existingEmployee.get();

                existing.setName(employee.getName());
                existing.setMobile(employee.getMobile());
                existing.setGender(employee.getGender());
                existing.setEmail(employee.getEmail());
                existing.setManager(employee.getManager());
                existing.setDateOfBirth(employee.getDateOfBirth());
                existing.setAddress(employee.getAddress());
                existing.setCity(employee.getCity());
                existing.setState(employee.getState());
                existing.setPostalCode(employee.getPostalCode());
                existing.setCountry(employee.getCountry());
                existing.setDepartment(employee.getDepartment());
                existing.setDesignation(employee.getDesignation());
                existing.setDateOfJoining(employee.getDateOfJoining());
                existing.setDateOfExit(employee.getDateOfExit());
                existing.setPanNumber(employee.getPanNumber());
                existing.setAadhaarNumber(employee.getAadhaarNumber());
                existing.setBankName(employee.getBankName());
                existing.setBankAccountNumber(employee.getBankAccountNumber());
                existing.setIfscCode(employee.getIfscCode());
                existing.setSalary(employee.getSalary());

                if (existing.getCreatedAt() == null) {
                    existing.setCreatedAt(LocalDateTime.now());
                }

                employeeRepository.save(existing);
                redirectAttributes.addFlashAttribute("success", "Employee updated successfully.");
                return "redirect:/employees";
            } else {
                redirectAttributes.addFlashAttribute("error", "Employee not found for update.");
                return "redirect:/employees?error=notfound";
            }
        }
        String empCode = "RCMEM" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        employee.setEmpCode(empCode);
        employeeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee saved successfully.");
        return "redirect:/employees";
    }

    @GetMapping("/edit/{empCode}")
    public String showEditForm(@PathVariable String empCode, Model model) {
        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee empCode"));
        model.addAttribute("employee", employee);
        model.addAttribute("formTitle", "Update Onboarded Employee Details");
        return "employee-edit";
    }

    @GetMapping("/delete/{empCode}")
    public String deleteEmployee(@PathVariable String empCode) {
        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new IllegalArgumentException("Employee with empCode " + empCode + " not found"));

        employeeRepository.delete(employee);
        return "redirect:/employees";
    }

    @PostMapping
    @ResponseBody
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    @GetMapping("/calculate-salary")
    public String calculateSalary(
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

                Employee employee = employeeRepository.findByEmpCode(empCode)
                        .orElseThrow(() -> new RuntimeException("Employee not found"));

                double monthlySalary = employee.getSalary();
                double dailyRate = monthlySalary / 30.0;
                double hourlyRate = dailyRate / 8.0;

                long totalWorkedMinutes = 0;
                long totalOvertimeMinutes = 0;
                long totalLateMinutes = 0;

                LocalTime shiftStart = LocalTime.of(9, 0);
                int standardWorkingMinutes = 8 * 60;

                for (Attendance att : attendanceList) {
                    if (att.getStatus() == Attendance.Status.PRESENT &&
                            att.getCheckInDateTime() != null &&
                            att.getCheckOutDateTime() != null) {

                        long dailyMinutes = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime()).toMinutes();
                        totalWorkedMinutes += dailyMinutes;

                        if (dailyMinutes > standardWorkingMinutes) {
                            totalOvertimeMinutes += (dailyMinutes - standardWorkingMinutes);
                        }

                        LocalTime checkIn = att.getCheckInDateTime().toLocalTime();
                        if (checkIn.isAfter(shiftStart)) {
                            totalLateMinutes += Duration.between(shiftStart, checkIn).toMinutes();
                        }
                    }
                }

                long presentDays = attendanceList.stream()
                        .filter(att -> att.getStatus() == Attendance.Status.PRESENT)
                        .count();

                double baseSalary = presentDays * dailyRate;
                double overtimePay = (totalOvertimeMinutes / 60.0) * hourlyRate;
                double totalSalary = baseSalary + overtimePay;

                String workedHours = (totalWorkedMinutes / 60) + " Hrs " + (totalWorkedMinutes % 60) + " Mins";
                String overtimeHours = (totalOvertimeMinutes / 60) + " Hrs " + (totalOvertimeMinutes % 60) + " Mins";
                String lateHours = (totalLateMinutes / 60) + " Hrs " + (totalLateMinutes % 60) + " Mins";

                BigDecimal baseSal = BigDecimal.valueOf(baseSalary).setScale(2, RoundingMode.HALF_UP);
                BigDecimal otPay = BigDecimal.valueOf(overtimePay).setScale(2, RoundingMode.HALF_UP);
                BigDecimal totalSal = BigDecimal.valueOf(totalSalary).setScale(2, RoundingMode.HALF_UP);

                model.addAttribute("employee", employee);
                model.addAttribute("attendanceList", attendanceList);
                model.addAttribute("presentDays", presentDays);

                model.addAttribute("workedHours", workedHours);
                model.addAttribute("overtimeHours", overtimeHours);
                model.addAttribute("lateHours", lateHours);

                model.addAttribute("baseSalary", baseSal);
                model.addAttribute("overtimePay", otPay);
                model.addAttribute("totalSalary", totalSal);
                String totalHoursWithOvertime = (totalWorkedMinutes / 60) + " Hrs " + (totalWorkedMinutes % 60) + " Mins";
                model.addAttribute("totalHoursWithOvertime", totalHoursWithOvertime);

            } catch (Exception ex) {
                model.addAttribute("errorMessage", "Employee not found or invalid data.");
            }
        }
        return "calculate-salary";
    }

    @GetMapping("/calculate-salary/pdf")
    public void downloadPayslip(
            @RequestParam String empCode,
            @RequestParam String start,
            @RequestParam String end,
            HttpServletResponse response) throws IOException, java.io.IOException {

        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        List<Attendance> attendanceList = attendanceService.getAttendance(empCode, s, e)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        double monthlySalary = employee.getSalary();
        double dailyRate = monthlySalary / 30.0;
        double hourlyRate = dailyRate / 8.0;

        double totalWorkedHours = 0.0;
        for (Attendance att : attendanceList) {
            if (att.getStatus() == Attendance.Status.PRESENT &&
                    att.getCheckInDateTime() != null &&
                    att.getCheckOutDateTime() != null) {
                Duration duration = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime());
                totalWorkedHours += duration.toMinutes() / 60.0;
            }
        }

        long presentDays = attendanceList.stream()
                .filter(att -> att.getStatus() == Attendance.Status.PRESENT)
                .count();
        double totalSalary = totalWorkedHours * hourlyRate;
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=payslip.pdf");

        PdfGeneratorUtil.generatePayslip(employee, attendanceList, presentDays, totalSalary, response.getOutputStream());
    }
}
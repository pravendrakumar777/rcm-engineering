package com.niiran.software.solutions.resource;

import com.itextpdf.io.exceptions.IOException;
import com.niiran.software.solutions.domain.Attendance;
import com.niiran.software.solutions.domain.Employee;
import com.niiran.software.solutions.domain.enumerations.EmployeeStatus;
import com.niiran.software.solutions.repository.EmployeeRepository;
import com.niiran.software.solutions.resource.utils.FtlToPdfUtil;
import com.niiran.software.solutions.service.AttendanceService;
import com.niiran.software.solutions.service.impl.EmployeeServiceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;
    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeRepository employeeRepository, AttendanceService attendanceService, EmployeeServiceImpl employeeService) {
        this.employeeRepository = employeeRepository;
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    // employee pre-onboarding list
    @GetMapping("/pre-onboarding")
    public String preOnboardingList(Model model) {
        List<Employee> onboardingEmployees = employeeRepository
                .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .filter(emp -> emp.getStatus() == EmployeeStatus.PENDING || emp.getStatus() == EmployeeStatus.CANCEL)
                .collect(Collectors.toList());

        model.addAttribute("employees", onboardingEmployees);
        model.addAttribute("onboardingPage", "employees");
        return "pre-onboarding-employee-list";
    }

    // employee post-onboarding list
    @GetMapping
    public String postOnboardingList(Model model) {
        List<Employee> activeEmployees = employeeRepository
                .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .filter(emp -> emp.getStatus() == EmployeeStatus.ACTIVE)
                .collect(Collectors.toList());
        model.addAttribute("employees", activeEmployees);
        model.addAttribute("activePage", "employees");
        return "employee-list";
    }

    // employee form
    @GetMapping("/form")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("formTitle", "Create New Employee");
        return "employee-form";
    }

    // employee creation
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam(value = "file", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) throws java.io.IOException {
        if (employee.getOhr() != null && !employee.getOhr().isEmpty()) {
            Optional<Employee> existingEmployee = employeeRepository.findByOhr(employee.getOhr());

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

                if (file != null && !file.isEmpty()) {
                    existing.setPhoto(file.getBytes());
                }

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
        String ohr = "NII" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
        employee.setOhr(ohr);

        if (file != null && !file.isEmpty()) {
            employee.setPhoto(file.getBytes());
        }
        employeeRepository.save(employee);
        redirectAttributes.addFlashAttribute("success", "Employee saved successfully.");
        return "redirect:/employees";
    }

    // fetch photo
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        byte[] photo = employee.getPhoto();
        if (photo == null || photo.length == 0) {
            throw new EntityNotFoundException("Photo not found for employee id: " + id);
        }
        return ResponseEntity.ok() .contentType(MediaType.IMAGE_JPEG).body(photo);
    }

    // employee update form
    @GetMapping("/edit/{ohr}")
    public String showEditForm(@PathVariable String ohr, Model model) {
        Employee employee = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ohr"));
        model.addAttribute("employee", employee);
        model.addAttribute("formTitle", "Update Onboarded Employee Details");
        return "employee-edit";
    }

    // employee update
    @PostMapping("/edit/{ohr}")
    public String updateEmployee(@PathVariable String ohr,
                                 @ModelAttribute Employee employee,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) throws IOException, java.io.IOException {

        Employee existing = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee ohr"));

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

        if (file != null && !file.isEmpty()) {
            existing.setPhoto(file.getBytes());
        }
        employeeRepository.save(existing);
        redirectAttributes.addFlashAttribute("success", "Employee updated successfully.");
        return "redirect:/employees";
    }


    // employee delete
    @GetMapping("/delete/{ohr}")
    public String deleteEmployee(@PathVariable String ohr) {
        Employee employee = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ohr " + ohr + " not found"));
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

    // salary calculation
    @GetMapping("/calculate-salary")
    public String calculateSalary(
            @RequestParam(required = false) String ohr,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            Model model) {

        if (ohr != null && start != null && end != null) {
            try {
                LocalDate s = LocalDate.parse(start);
                LocalDate e = LocalDate.parse(end);

                List<Attendance> attendanceList = attendanceService.getAttendance(ohr, s, e)
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                Employee employee = employeeRepository.findByOhr(ohr)
                        .orElseThrow(() -> new RuntimeException("Employee not found"));

                double monthlySalary = employee.getSalary();
                long workingDays = ChronoUnit.DAYS.between(s, e) + 1;
                int workingHoursPerDay = 8;
                long totalMinutesInPeriod = workingDays * workingHoursPerDay * 60;

                Duration totalDuration = Duration.ZERO;
                long presentDays = 0;
                long absentDays = 0;
                for (Attendance att : attendanceList) {
                    if (att.getStatus() == Attendance.Status.PRESENT) {
                        presentDays++;
                    } else if (att.getStatus() == Attendance.Status.ABSENT) {
                        absentDays++;
                    }
                    if (att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null) {
                        Duration dailyDuration = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime());
                        totalDuration = totalDuration.plus(dailyDuration);
                    }
                }

                long totalWorkedMinutes = totalDuration.toMinutes();
                String workedHours = (totalWorkedMinutes / 60) + " Hrs " + (totalWorkedMinutes % 60) + " Mins";
                double perMinuteRate = monthlySalary / (30.0 * workingHoursPerDay * 60.0);
                double totalSalary = totalWorkedMinutes * perMinuteRate;

                model.addAttribute("employee", employee);
                model.addAttribute("attendanceList", attendanceList);
                model.addAttribute("workingDays", workingDays);
                model.addAttribute("presentDays", presentDays);
                model.addAttribute("absentDays", absentDays);
                model.addAttribute("workedHours", workedHours);
                model.addAttribute("monthlySalary", monthlySalary);
                model.addAttribute("perMinuteRate", BigDecimal.valueOf(perMinuteRate).setScale(2, RoundingMode.HALF_UP));
                model.addAttribute("totalSalary", BigDecimal.valueOf(totalSalary).setScale(2, RoundingMode.HALF_UP));

            } catch (Exception ex) {
                model.addAttribute("errorMessage", "Employee not found or invalid data.");
            }
        }
        return "calculate-salary";
    }

    // salary payslip
    @GetMapping("/calculate-salary/pdf")
    public void downloadPayslip(
            @RequestParam String ohr,
            @RequestParam String start,
            @RequestParam String end,
            HttpServletResponse response) throws IOException, java.io.IOException {

        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        List<Attendance> attendanceList = attendanceService.getAttendance(ohr, s, e)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Employee employee = employeeRepository.findByOhr(ohr)
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
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM_yyyy"));
        String emCode = employee.getOhr();
        String fileName = emCode + "_" + currentMonth + "_payslip.pdf";

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName);
        FtlToPdfUtil.generatePayslip(employee, attendanceList, presentDays, totalSalary, response.getOutputStream());
    }

    // download Employee Profile
    @GetMapping("/{ohr}/profile")
    public ResponseEntity<byte[]> profilePDF(@PathVariable String ohr) {
        try {
            byte[] pdfBytes = employeeService.generateEmployeeProfilePdf(ohr);
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM_yyyy"));
            String fileName = ohr + "_" + currentMonth + "_profile.pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
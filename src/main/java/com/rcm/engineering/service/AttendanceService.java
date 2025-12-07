package com.rcm.engineering.service;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.AttendanceRepository;
import com.rcm.engineering.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    public Attendance markAttendance(String empCode, LocalDate date, Attendance.Status status,
                                     LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime) {

        Optional<Employee> empOpt = employeeRepository.findByEmpCode(empCode);
        if (!empOpt.isPresent()) {
            throw new IllegalArgumentException("Employee with empCode " + empCode + " not found.");
        }
        Employee emp = empOpt.get();
        // Block attendance if employee status is PENDING or CANCEL
        if (emp.getStatus() == EmployeeStatus.PENDING || emp.getStatus() == EmployeeStatus.CANCEL) {
            throw new IllegalStateException("Attendance cannot be marked for employee with status " + emp.getStatus());
        }
        Optional<Attendance> existingOpt = attendanceRepository.findByEmployeeAndDate(emp, date);
        Attendance attendance;

        if (!existingOpt.isPresent()) {
            attendance = new Attendance(emp, date, status, checkInDateTime, null);
            if (checkInDateTime != null) {
                attendance.setCheckInDateTime(checkInDateTime);
            }
        } else {
            attendance = existingOpt.get();
            if (attendance.getCheckInDateTime() == null && checkInDateTime != null) {
                attendance.setCheckInDateTime(checkInDateTime);
                attendance.setStatus(status);
            } else if (attendance.getCheckOutDateTime() == null && checkOutDateTime != null) {
                attendance.setCheckOutDateTime(checkOutDateTime);
            }
        }
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendance(String empCode, LocalDate start, LocalDate end) {
        Employee emp = employeeRepository.findByEmpCode(empCode).orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Attendance> existingRecords = attendanceRepository.findByEmployeeAndDateBetween(emp, start, end);
        Map<LocalDate, Attendance> recordMap = existingRecords.stream()
                .collect(Collectors.toMap(Attendance::getDate, Function.identity()));

        List<Attendance> completeRecords = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Attendance att = recordMap.getOrDefault(date, createAbsentRecord(emp, date));
            completeRecords.add(att);
        }
        return completeRecords;
    }

    private Attendance createAbsentRecord(Employee emp, LocalDate date) {
        Attendance absent = new Attendance();
        absent.setEmployee(emp);
        absent.setDate(date);
        absent.setStatus(Attendance.Status.ABSENT);
        absent.setCheckInDateTime(null);
        absent.setCheckOutDateTime(null);
        return absent;
    }

    public List<Attendance> getAllAttendance(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }
}

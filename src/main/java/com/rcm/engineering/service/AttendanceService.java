package com.rcm.engineering.service;

import com.rcm.engineering.domain.Attendance;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceService {

    Attendance markAttendance(String ohr, LocalDate date, Attendance.Status status, LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime);
    List<Attendance> getAttendance(String ohr, LocalDate start, LocalDate end);
    List<Attendance> getAllAttendance(LocalDate startDate, LocalDate endDate);
    List<Attendance> getAttendanceByOhr(String ohr);
    List<Attendance> getAttendanceBetween(LocalDate fromDate, LocalDate toDate);
    List<Attendance> getAttendanceForEmployeeBetween(String ohr, LocalDate fromDate, LocalDate toDate);
    List<Attendance> getAllAttendances();
    Workbook writeAttendancesToExcel();
}

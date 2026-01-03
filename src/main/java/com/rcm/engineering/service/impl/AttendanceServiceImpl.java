package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.domain.enumerations.EmployeeStatus;
import com.rcm.engineering.repository.AttendanceRepository;
import com.rcm.engineering.repository.EmployeeRepository;
import com.rcm.engineering.service.AttendanceService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private static final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Attendance markAttendance(String ohr, LocalDate date, Attendance.Status status,
                                     LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime) {

        log.trace("markAttendance:START | ohr:{} | date:{} | status:{} | checkInDateTime:{} | checkOutDateTime:{}",
                ohr, date, status, checkInDateTime, checkOutDateTime);

        Optional<Employee> empOpt = employeeRepository.findByOhr(ohr);
        if (!empOpt.isPresent()) {
            log.error("markAttendance:ERROR | EMPLOYEE_NOT_FOUND | empCode:{}",
                    ohr);
            throw new IllegalArgumentException("Employee with empCode " + ohr + " not found.");
        }

        Employee emp = empOpt.get();
        log.trace("markAttendance:EMPLOYEE_LOADED | empStatus:{}",
                emp.getStatus());

        if (emp.getStatus() == EmployeeStatus.PENDING || emp.getStatus() == EmployeeStatus.CANCEL) {
            log.error("markAttendance:ERROR | INVALID_EMPLOYEE_STATUS | empStatus:{}",
                    emp.getStatus());
            throw new IllegalStateException(
                    "Attendance cannot be marked for employee with status " + emp.getStatus());
        }

        Optional<Attendance> existingOpt = attendanceRepository.findByEmployeeAndDate(emp, date);
        Attendance attendance;

        if (!existingOpt.isPresent()) {
            log.trace("markAttendance:ATTENDANCE_CREATE | empCode:{} | date:{}", ohr, date);
            attendance = new Attendance(emp, date, status, checkInDateTime, null);

            if (checkInDateTime != null) {
                log.trace("markAttendance:SET_CHECKIN | checkInDateTime:{}", checkInDateTime);
                attendance.setCheckInDateTime(checkInDateTime);
            }

        } else {
            attendance = existingOpt.get();
            log.trace("markAttendance:ATTENDANCE_EXISTING | checkInDateTime:{} | checkOutDateTime:{}",
                    attendance.getCheckInDateTime(),
                    attendance.getCheckOutDateTime());

            if (attendance.getCheckInDateTime() == null && checkInDateTime != null) {
                log.trace("markAttendance:UPDATE_CHECKIN | checkInDateTime:{} | status:{}", checkInDateTime, status);
                attendance.setCheckInDateTime(checkInDateTime);
                attendance.setStatus(status);

            } else if (attendance.getCheckOutDateTime() == null && checkOutDateTime != null) {
                log.trace("markAttendance:UPDATE_CHECKOUT | checkOutDateTime:{}", checkOutDateTime);
                attendance.setCheckOutDateTime(checkOutDateTime);

            } else {
                log.trace("markAttendance:NO_UPDATE");
            }
        }

        log.trace("markAttendance:SAVE");
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> getAttendance(String ohr, LocalDate start, LocalDate end) {
        log.info("GET_ATTENDANCE | Request received | ohr: {} | start: {} | end: {}", ohr, start, end);

        Employee emp = employeeRepository.findByOhr(ohr)
                .orElseThrow(() -> {
                    log.error("GET_ATTENDANCE | Employee not found | empCode: {}", ohr);
                    return new RuntimeException("Employee not found");
                });

        List<Attendance> existingRecords = attendanceRepository.findByEmployeeAndDateBetween(emp, start, end);
        log.info("GET_ATTENDANCE | Existing records fetched | count: {}", existingRecords.size());

        Map<LocalDate, Attendance> recordMap = existingRecords.stream()
                .collect(Collectors.toMap(Attendance::getDate, Function.identity()));
        log.debug("GET_ATTENDANCE | Record map prepared | keys: {}", recordMap.keySet());

        List<Attendance> completeRecords = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            Attendance att = recordMap.getOrDefault(date, createAbsentRecord(emp, date));
            completeRecords.add(att);
            log.trace("GET_ATTENDANCE | Processed date: {} | status: {}", date, att.getStatus());
        }

        log.info("GET_ATTENDANCE | Completed | ohr: {} | total records: {}", ohr, completeRecords.size());
        return completeRecords;
    }

    private Attendance createAbsentRecord(Employee emp, LocalDate date) {
        log.info("CREATE_ABSENT_RECORD | Start | empCode: {} | date: {}", emp.getOhr(), date);
        Attendance absent = new Attendance();
        absent.setEmployee(emp);
        absent.setDate(date);
        absent.setStatus(Attendance.Status.ABSENT);
        absent.setCheckInDateTime(null);
        absent.setCheckOutDateTime(null);
        log.debug("CREATE_ABSENT_RECORD | Absent record created | empCode: {} | date: {} | status: {}", emp.getOhr(), date, absent.getStatus());
        log.info("CREATE_ABSENT_RECORD | End | empCode: {} | date: {}", emp.getOhr(), date);
        return absent;
    }

    @Override
    public List<Attendance> getAllAttendance(LocalDate startDate, LocalDate endDate) {
        log.info("GET_ALL_ATTENDANCE | Request received | startDate: {} | endDate: {}", startDate, endDate);

        List<Attendance> records = attendanceRepository.findByDateBetween(startDate, endDate);

        log.info("GET_ALL_ATTENDANCE | Records fetched | startDate: {} | endDate: {} | count: {}",
                startDate, endDate, records.size());

        if (records.isEmpty()) {
            log.warn("GET_ALL_ATTENDANCE | No records found | startDate: {} | endDate: {}", startDate, endDate);
        }
        return records;
    }


    @Override
    public List<Attendance> getAttendanceByOhr(String ohr) {
        log.info("GET_ATTENDANCE_BY_EMPCODE | Request received | empCode: {}", ohr);

        List<Attendance> records = attendanceRepository.findByEmployee_Ohr(ohr);

        log.info("GET_ATTENDANCE_BY_EMPCODE | Records fetched | ohr: {} | count: {}", ohr, records.size());

        if (records.isEmpty()) {
            log.warn("GET_ATTENDANCE_BY_EMPCODE | No records found | empCode: {}", ohr);
        } else {
            log.debug("GET_ATTENDANCE_BY_EMPCODE | First record sample | empCode: {} | date: {} | status: {}",
                    ohr,
                    records.get(0).getDate(),
                    records.get(0).getStatus());
        }
        return records;
    }

    @Override
    public List<Attendance> getAttendanceBetween(LocalDate fromDate, LocalDate toDate) {
        log.info("GET_ATTENDANCE_BETWEEN | Request received | fromDate: {} | toDate: {}", fromDate, toDate);

        List<Attendance> records = attendanceRepository.findByDateBetween(fromDate, toDate);

        log.info("GET_ATTENDANCE_BETWEEN | Records fetched | fromDate: {} | toDate: {} | count: {}",
                fromDate, toDate, records.size());

        if (records.isEmpty()) {
            log.warn("GET_ATTENDANCE_BETWEEN | No records found | fromDate: {} | toDate: {}", fromDate, toDate);
        } else {
            log.debug("GET_ATTENDANCE_BETWEEN | Sample record | empCode: {} | date: {} | status: {}",
                    records.get(0).getEmployee().getOhr(),
                    records.get(0).getDate(),
                    records.get(0).getStatus());
        }
        return records;
    }

    @Override
    public List<Attendance> getAttendanceForEmployeeBetween(String ohr, LocalDate fromDate, LocalDate toDate) {
        log.info("GET_ATTENDANCE_FOR_EMPLOYEE_BETWEEN | Request received | empCode: {} | fromDate: {} | toDate: {}",
                ohr, fromDate, toDate);

        List<Attendance> records = attendanceRepository.findByEmployeeOhrAndDateBetween(ohr, fromDate, toDate);

        log.info("GET_ATTENDANCE_FOR_EMPLOYEE_BETWEEN | Records fetched | ohr: {} | fromDate: {} | toDate: {} | count: {}",
                ohr, fromDate, toDate, records.size());

        if (records.isEmpty()) {
            log.warn("GET_ATTENDANCE_FOR_EMPLOYEE_BETWEEN | No records found | ohr: {} | fromDate: {} | toDate: {}",
                    ohr, fromDate, toDate);
        } else {
            log.debug("GET_ATTENDANCE_FOR_EMPLOYEE_BETWEEN | Sample record | ohr: {} | date: {} | status: {}",
                    ohr,
                    records.get(0).getDate(),
                    records.get(0).getStatus());
        }
        return records;
    }

    @Override
    public List<Attendance> getAllAttendances() {
        log.info("GET_ALL_ATTENDANCES | Request received");

        List<Attendance> records = attendanceRepository.findAll();

        log.info("GET_ALL_ATTENDANCES | Records fetched | count: {}", records.size());

        if (records.isEmpty()) {
            log.warn("GET_ALL_ATTENDANCES | No records found");
        } else {
            log.debug("GET_ALL_ATTENDANCES | Sample record | empCode: {} | date: {} | status: {}",
                    records.get(0).getEmployee().getOhr(),
                    records.get(0).getDate(),
                    records.get(0).getStatus());
        }
        return records;
    }

    @Override
    public Workbook writeAttendancesToExcel() {
        log.info("WRITE_ATTENDANCES_TO_EXCEL | Start");

        List<Attendance> attendances = getAllAttendances();
        log.info("WRITE_ATTENDANCES_TO_EXCEL | Records fetched | count: {}", attendances.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");
        log.debug("WRITE_ATTENDANCES_TO_EXCEL | Workbook and sheet created");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("OHR");
        header.createCell(1).setCellValue("Date");
        header.createCell(2).setCellValue("Status");
        header.createCell(3).setCellValue("CheckIn");
        header.createCell(4).setCellValue("CheckOut");
        log.debug("WRITE_ATTENDANCES_TO_EXCEL | Header row created");

        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm"));

        int rowIdx = 1;
        for (Attendance att : attendances) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(att.getEmployee().getOhr());

            if (att.getDate() != null) {
                Cell cell = row.createCell(1);
                cell.setCellValue(Date.valueOf(att.getDate()));
                cell.setCellStyle(dateStyle);
            }

            row.createCell(2).setCellValue(att.getStatus().toString());

            if (att.getCheckInDateTime() != null) {
                Cell cell = row.createCell(3);
                cell.setCellValue(Timestamp.valueOf(att.getCheckInDateTime()));
                cell.setCellStyle(dateStyle);
            }

            if (att.getCheckOutDateTime() != null) {
                Cell cell = row.createCell(4);
                cell.setCellValue(Timestamp.valueOf(att.getCheckOutDateTime()));
                cell.setCellStyle(dateStyle);
            }

            log.trace("WRITE_ATTENDANCES_TO_EXCEL | Row created | empCode: {} | date: {} | status: {}",
                    att.getEmployee().getOhr(), att.getDate(), att.getStatus());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
        log.info("WRITE_ATTENDANCES_TO_EXCEL | Completed | total rows: {}", rowIdx);

        return workbook;
    }
}

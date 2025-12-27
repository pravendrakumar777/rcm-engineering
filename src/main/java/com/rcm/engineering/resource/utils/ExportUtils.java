package com.rcm.engineering.resource.utils;

import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.domain.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExportUtils {
    public static void writeAttendanceCSV(List<Attendance> records, OutputStream os) throws IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));

        printWriter.println("EMP CODE, NAME, DATE, STATUS, CHECK-IN, CHECK-OUT, EMAIL, MOBILE, MANAGER, DEPARTMENT, " +
                "DESIGNATION, DOB, DOJ, SALARY, PAN, AADHAAR, BANK, ACCOUNT, IFSC, ADDRESS, CITY, STATE, COUNTRY");
        for (Attendance record : records) {
            Employee emp = record.getEmployee();
            printWriter.printf("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s%n",
                    emp.getEmpCode(),
                    emp.getName(),
                    record.getDate(),
                    record.getStatus(),
                    record.getCheckInDateTime() != null ? record.getCheckInDateTime() : "",
                    record.getCheckOutDateTime() != null ? record.getCheckOutDateTime() : "",
                    emp.getEmail(),
                    emp.getMobile(),
                    emp.getManager(),
                    emp.getDepartment(),
                    emp.getDesignation(),
                    emp.getDateOfBirth(),
                    emp.getDateOfJoining(),
                    emp.getSalary(),
                    emp.getPanNumber(),
                    emp.getAadhaarNumber(),
                    emp.getBankName(),
                    emp.getBankAccountNumber(),
                    emp.getIfscCode(),
                    emp.getAddress(),
                    emp.getCity(),
                    emp.getState(),
                    emp.getCountry()
            );
        }
        printWriter.flush();
    }

    public static void writeAttendanceExcel(List<Attendance> records, OutputStream os) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance");

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "EMP CODE", "NAME", "DATE", "STATUS", "CHECK-IN", "CHECK-OUT", "EMAIL", "MOBILE",
                    "MANAGER", "DEPARTMENT", "DESIGNATION", "DOB", "DOJ", "SALARY", "PAN", "AADHAAR",
                    "BANK", "ACCOUNT", "IFSC", "ADDRESS", "CITY", "STATE", "COUNTRY"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Attendance record : records) {
                Employee emp = record.getEmployee();
                Row row = sheet.createRow(rowIdx++);

                int col = 0;
                row.createCell(col++).setCellValue(emp.getEmpCode());
                row.createCell(col++).setCellValue(emp.getName());
                row.createCell(col++).setCellValue(record.getDate().toString());
                row.createCell(col++).setCellValue(
                        record.getStatus() != null ? record.getStatus().toString() : ""
                );
                row.createCell(col++).setCellValue(record.getCheckInDateTime() != null ? record.getCheckInDateTime().toString() : "");
                row.createCell(col++).setCellValue(record.getCheckOutDateTime() != null ? record.getCheckOutDateTime().toString() : "");
                row.createCell(col++).setCellValue(emp.getEmail());
                row.createCell(col++).setCellValue(emp.getMobile());
                row.createCell(col++).setCellValue(emp.getManager());
                row.createCell(col++).setCellValue(emp.getDepartment());
                row.createCell(col++).setCellValue(emp.getDesignation());
                row.createCell(col++).setCellValue(emp.getDateOfBirth() != null ? emp.getDateOfBirth().toString() : "");
                row.createCell(col++).setCellValue(emp.getDateOfJoining() != null ? emp.getDateOfJoining().toString() : "");
                row.createCell(col++).setCellValue(emp.getSalary() != null ? emp.getSalary().toString() : "");
                row.createCell(col++).setCellValue(emp.getPanNumber());
                row.createCell(col++).setCellValue(emp.getAadhaarNumber());
                row.createCell(col++).setCellValue(emp.getBankName());
                row.createCell(col++).setCellValue(emp.getBankAccountNumber());
                row.createCell(col++).setCellValue(emp.getIfscCode());
                row.createCell(col++).setCellValue(emp.getAddress());
                row.createCell(col++).setCellValue(emp.getCity());
                row.createCell(col++).setCellValue(emp.getState());
                row.createCell(col++).setCellValue(emp.getCountry());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(os);
        }
    }

    public static byte[] generateChallanExcel(Challan challan) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Challan");

            int rowIdx = 0;
            // Header
            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("Challan No");
            header.createCell(1).setCellValue("Ref No");
            header.createCell(2).setCellValue("Vendor Partner’s");
            header.createCell(3).setCellValue("Date");

            Row headerValues = sheet.createRow(rowIdx++);
            headerValues.createCell(0).setCellValue(challan.getChallanNo());
            headerValues.createCell(1).setCellValue(challan.getRefChNo());
            headerValues.createCell(2).setCellValue(challan.getCustomerName());
            headerValues.createCell(3).setCellValue(challan.getDate().toString());

            // Items Header
            Row itemHeader = sheet.createRow(rowIdx++);
            itemHeader.createCell(0).setCellValue("Item");
            itemHeader.createCell(1).setCellValue("Weight");
            //itemHeader.createCell(2).setCellValue("Quantity");
            itemHeader.createCell(2).setCellValue("Rate/Piece");
            itemHeader.createCell(3).setCellValue("Total Pieces");
            itemHeader.createCell(4).setCellValue("Amount");

            // Items
            for (ChallanItem item : challan.getItems()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getDescription());
                row.createCell(1).setCellValue(item.getWeight());
                //row.createCell(2).setCellValue(item.getQuantity());
                row.createCell(2).setCellValue(item.getRatePerPiece());
                row.createCell(3).setCellValue(item.getTotalPieces());
                row.createCell(4).setCellValue(item.getTotalAmount());
            }

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public static byte[] generateChallanCSV(Challan challan) throws Exception {
        StringBuilder sb = new StringBuilder();
        // Header row
        sb.append("Challan No,Ref No,Vendor Partner’s,Date\n");
        sb.append(challan.getChallanNo()).append(",")
                .append(challan.getRefChNo()).append(",")
                .append(challan.getCustomerName()).append(",")
                .append(challan.getDate().toString()).append("\n\n");

        // Items header
        sb.append("Item,Weight,Rate/Piece,Total Pieces,Amount\n");

        // Items rows
        for (ChallanItem item : challan.getItems()) {
            sb.append(item.getDescription()).append(",")
                    .append(item.getWeight()).append(",")
                    //.append(item.getQuantity()).append(",")
                    .append(item.getRatePerPiece()).append(",")
                    .append(item.getTotalPieces()).append(",")
                    .append(item.getTotalAmount()).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}

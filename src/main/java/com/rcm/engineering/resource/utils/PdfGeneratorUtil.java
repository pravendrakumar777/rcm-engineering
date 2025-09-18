package com.rcm.engineering.resource.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.domain.Employee;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGeneratorUtil {

    private static String nonNull(String value) {
        return value == null ? "" : value;
    }

    public static byte[] generateFtlChallanPDF(Challan challan) throws Exception {
        String htmlContent = FtlToPdfUtil.processTemplateForChallan(challan);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, out);
        return out.toByteArray();
    }

    public static byte[] generateFtlPayslipPDF(Employee emp,
                                            List<Attendance> records,
                                            long presentDays,
                                            double totalSalary) throws Exception {
        //String htmlContent = FtlToPdfUtil.processTemplateForPayslip(emp, records, presentDays, totalSalary);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //HtmlConverter.convertToPdf(htmlContent, out);
        return out.toByteArray();
    }

    public static void generatePayslip(Employee emp, List<Attendance> records, long presentDays, double totalSalary, OutputStream out) {
        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 3}));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            try (InputStream logoStream = PdfGeneratorUtil.class.getResourceAsStream("/static/images/logo.png")) {
                if (logoStream != null) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int nRead;
                    while ((nRead = logoStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    byte[] imageBytes = buffer.toByteArray();
                    Image logo = new Image(ImageDataFactory.create(imageBytes)).scaleAbsolute(60, 60);
                    headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
                } else {
                    headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                }
            } catch (Exception e) {
                headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            }

            Paragraph companyName = new Paragraph("RCM ENGINEERING & MANUFACTURING")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.LEFT);
            Paragraph companyDetails = new Paragraph(
                    "KH NO: 513/1, 513/2,\n" +
                            "VILL BASAI, NEAR BASAI FLYOVER - GURUGRAM HR. 122001\n" +
                            "Mob: 9639200584, 7819929402 | Email: cs29680881@gmail.com\n" +
                            "GSTIN: 06ABCDE1234F1Z5 | CIN: U12345UP2020PTC123456"
            )
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(2);
            Cell companyCell = new Cell()
                    .add(companyName)
                    .add(companyDetails)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(Border.NO_BORDER);
            headerTable.addCell(companyCell);
            doc.add(headerTable);
            if (!records.isEmpty()) {
                LocalDate firstDate = records.get(0).getDate();
                String monthYear = firstDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                doc.add(new Paragraph("Payslip for the Month of " + monthYear)
                        .setBold()
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = LocalDate.now().format(formatter);
            doc.add(new Paragraph("Payslip Issued Date: " + formattedDate));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Employee Details").setBold().setFontSize(14));
            Table empTwoColumnTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
            empTwoColumnTable.setWidth(UnitValue.createPercentValue(100));
            Table leftTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
            leftTable.setWidth(UnitValue.createPercentValue(100));
            leftTable.addCell(new Cell().add(new Paragraph("EMP CODE").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(emp.getEmpCode())));
            leftTable.addCell(new Cell().add(new Paragraph("Name").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(emp.getName())));
            leftTable.addCell(new Cell().add(new Paragraph("Mobile").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(emp.getMobile())));
            leftTable.addCell(new Cell().add(new Paragraph("PAN").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(emp.getPanNumber())));
            DateTimeFormatter commonFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String strDob = emp.getDateOfBirth().format(commonFormat);
            String strDoJ = emp.getDateOfJoining().format(commonFormat);
            leftTable.addCell(new Cell().add(new Paragraph("DOB").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(strDob)));
            leftTable.addCell(new Cell().add(new Paragraph("DOJ").setBold()));
            leftTable.addCell(new Cell().add(new Paragraph(strDoJ)));
            Table rightTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
            rightTable.setWidth(UnitValue.createPercentValue(100));
            rightTable.addCell(new Cell().add(new Paragraph("Department").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getDepartment())));
            rightTable.addCell(new Cell().add(new Paragraph("Designation").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getDesignation())));
            rightTable.addCell(new Cell().add(new Paragraph("Manager").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getManager())));
            rightTable.addCell(new Cell().add(new Paragraph("Bank Name").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getBankName())));
            rightTable.addCell(new Cell().add(new Paragraph("A/C No.").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getBankAccountNumber())));
            rightTable.addCell(new Cell().add(new Paragraph("IFSC").setBold()));
            rightTable.addCell(new Cell().add(new Paragraph(emp.getIfscCode())));
            empTwoColumnTable.addCell(new Cell().add(leftTable).setBorder(Border.NO_BORDER));
            empTwoColumnTable.addCell(new Cell().add(rightTable).setBorder(Border.NO_BORDER));
            doc.add(empTwoColumnTable);
            Table salaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 7}));
            salaryTable.setWidth(UnitValue.createPercentValue(100));
            salaryTable.addCell(new Cell().add(new Paragraph("Monthly Salary (₹)").setBold()));
            salaryTable.addCell(new Cell().add(new Paragraph("₹" + String.format("%.2f", emp.getSalary()) + " /-")));
            salaryTable.addCell(new Cell().add(new Paragraph("Net Pay (₹)").setBold()));
            salaryTable.addCell(new Cell().add(new Paragraph("₹" + String.format("%.2f", totalSalary) + " /-")));
            doc.add(salaryTable);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Attendance Summary").setBold().setFontSize(14));
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Check-In").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Check-Out").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Working Hours").setBold()));

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            Duration totalDuration = Duration.ZERO;
            int presentCount = 0;
            int absentCount = 0;

            for (Attendance att : records) {

                String formatDate = att.getDate().format(dateFormatter);
                String checkIn = att.getCheckInDateTime() != null ? att.getCheckInDateTime().format(timeFormatter).toUpperCase() : "—";
                String checkOut = att.getCheckOutDateTime() != null ? att.getCheckOutDateTime().format(timeFormatter).toUpperCase() : "—";

                if ("PRESENT".equalsIgnoreCase(att.getStatus().toString())) {
                    presentCount++;
                } else if ("ABSENT".equalsIgnoreCase(att.getStatus().toString())) {
                    absentCount++;
                }
                if (att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null) {
                    Duration dailyDuration = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime());
                    totalDuration = totalDuration.plus(dailyDuration);
                }
                table.addCell(formatDate);
                table.addCell(att.getStatus().toString());
                table.addCell(checkIn);
                table.addCell(checkOut);
                table.addCell(att.getFormattedTotalHours());
            }

            long totalMinutes = totalDuration.toMinutes();
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            String formattedTotal = String.format("%d Hrs %d Mins", hours, minutes);
            String statusSummary = String.format("P-%dd, A-%dd", presentCount, absentCount);

            table.addCell(new Cell().add(new Paragraph("Status Summary").setBold()));
            table.addCell(new Cell().add(new Paragraph(statusSummary).setBold()));
            table.addCell(new Cell().add(new Paragraph("")));
            table.addCell(new Cell().add(new Paragraph("Total Hours").setBold())
                    .setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(new Paragraph(formattedTotal).setBold())
                    .setTextAlignment(TextAlignment.RIGHT));
            doc.add(table);
            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}

package com.rcm.engineering.resource.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.domain.Employee;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FtlToPdfUtil {

    // challan PDF
    public static byte[] generateChallanPDF(Challan challan) throws Exception {
        String htmlContent = processTemplateForChallan(challan);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, out);
        return out.toByteArray();
    }

    static String processTemplateForChallan(Challan challan) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(
                PdfGeneratorUtil.class.getClassLoader(),
                "/templates"
        );
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Template template = cfg.getTemplate("challan.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put("challan", challan);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        model.put("formattedDate", challan.getDate().format(formatter));
        double grandTotal = challan.getItems()
                .stream()
                .mapToDouble(ChallanItem::getTotalAmount)
                .sum();
        model.put("grandTotal", grandTotal);
        try (StringWriter out = new StringWriter()) {
            template.process(model, out);
            return out.toString();
        }
    }

    // payslip PDF
    public static void generatePayslipPDF(Employee emp, List<Attendance> records, long presentDays, double totalSalary) throws Exception {
        String htmlContent = processTemplateForPayslip(emp, records, presentDays, totalSalary);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, out);
    }

    public static String processTemplateForPayslip(Employee emp,
                                                   List<Attendance> records,
                                                   long presentDays,
                                                   double totalSalary) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(
                PdfGeneratorUtil.class.getClassLoader(),
                "templates"
        );
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template template = cfg.getTemplate("payslip.ftl");

        Map<String, Object> model = new HashMap<>();
        model.put("emp", emp);
        model.put("presentDays", presentDays);
        model.put("totalSalary", totalSalary);

        String logoPath = PdfGeneratorUtil.class
                .getResource("/static/images/logo.png")
                .toExternalForm();
        model.put("logoPath", logoPath);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        model.put("dob", emp.getDateOfBirth() != null ? emp.getDateOfBirth().format(dateFormatter) : "");
        model.put("doj", emp.getDateOfJoining() != null ? emp.getDateOfJoining().format(dateFormatter) : "");

        if (!records.isEmpty() && records.get(0).getDate() != null) {
            LocalDate firstDate = records.get(0).getDate();
            model.put("monthYear", firstDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        } else {
            model.put("monthYear", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        }

        String issuedDate = LocalDate.now().format(dateFormatter);
        model.put("issuedDate", issuedDate);
        model.put("formattedDate", issuedDate);

        Duration totalDuration = Duration.ZERO;
        int absentCount = 0;

        List<Map<String, Object>> formattedRecords = new ArrayList<>();
        for (Attendance att : records) {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put("status", att.getStatus());
            recordMap.put("formattedDate", att.getDate() != null ? att.getDate().format(dateFormatter) : "");
            recordMap.put("formattedCheckIn", att.getCheckInDateTime() != null ? att.getCheckInDateTime().format(timeFormatter) : "");
            recordMap.put("formattedCheckOut", att.getCheckOutDateTime() != null ? att.getCheckOutDateTime().format(timeFormatter) : "");

            String formattedTotalHours = "";
            if (att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null) {
                Duration dailyDuration = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime());
                long mins = dailyDuration.toMinutes();
                long hrs = mins / 60;
                long remMins = mins % 60;
                formattedTotalHours = String.format("%d Hrs %d Mins", hrs, remMins);
                totalDuration = totalDuration.plus(dailyDuration);
            }
            recordMap.put("formattedTotalHours", formattedTotalHours);

            if ("ABSENT".equalsIgnoreCase(String.valueOf(att.getStatus()))) {
                absentCount++;
            }

            formattedRecords.add(recordMap);
        }
        model.put("records", formattedRecords);

        long totalMinutes = totalDuration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        String formattedTotal = String.format("%d Hrs %d Mins", hours, minutes);

        model.put("absentDays", absentCount);
        model.put("totalHours", formattedTotal);

        try (StringWriter out = new StringWriter()) {
            template.process(model, out);
            return out.toString();
        }
    }
}

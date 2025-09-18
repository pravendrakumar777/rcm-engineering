package com.rcm.engineering.resource.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.domain.Employee;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FtlToPdfUtil {

    // challan PDF
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

    public static byte[] generateChallanPDF(Challan challan) throws Exception {
        String htmlContent = processTemplateForChallan(challan);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, out);
        return out.toByteArray();
    }

    // payslip PDF
    public static void generatePayslip(Employee emp,
                                       List<Attendance> records,
                                       long presentDays,
                                       double totalSalary,
                                       OutputStream out) {

        try {
            Map<String, Object> model = new HashMap<>();
            model.put("emp", emp);
            model.put("records", records);
            model.put("presentDays", presentDays);
            model.put("totalSalary", totalSalary);

            if (!records.isEmpty()) {
                LocalDate firstDate = records.get(0).getDate();
                String monthYear = firstDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                model.put("monthYear", monthYear);
            }
            DateTimeFormatter commonFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String strDob = emp.getDateOfBirth().format(commonFormat);
            String strDoj = emp.getDateOfJoining().format(commonFormat);

            model.put("dob", strDob);
            model.put("doj", strDoj);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            List<Map<String, Object>> formattedRecords = records.stream().map(att -> {
                Map<String, Object> m = new HashMap<>();
                m.put("date", att.getDate() != null ? att.getDate().format(dateFormatter) : "—");
                m.put("status", att.getStatus().toString());
                m.put("checkIn", att.getCheckInDateTime() != null ? att.getCheckInDateTime().format(timeFormatter).toUpperCase() : "—");
                m.put("checkOut", att.getCheckOutDateTime() != null ? att.getCheckOutDateTime().format(timeFormatter).toUpperCase() : "—");
                m.put("totalHours", att.getFormattedTotalHours());
                return m;
            }).collect(Collectors.toList());

            model.put("records", formattedRecords);
            double totalWorkedHours = records.stream()
                    .mapToDouble(att -> {
                        if (att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null) {
                            return java.time.Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime()).toMinutes() / 60.0;
                        }
                        return 0.0;
                    }).sum();
            model.put("totalWorkedHours", String.format("%.2f Hrs", totalWorkedHours));
            model.put("logoPath", "/static/images/logo.png");
            Configuration freemarkerConfig = getFreemarkerConfig();
            Template template = freemarkerConfig.getTemplate("payslip.ftl");
            StringWriter stringWriter = new StringWriter();
            template.process(model, stringWriter);
            String htmlContent = stringWriter.toString();
            HtmlConverter.convertToPdf(htmlContent, out);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private static Configuration getFreemarkerConfig() throws IOException {
        Configuration cfg = new Configuration(new Version("2.3.32"));
        cfg.setClassForTemplateLoading(FtlToPdfUtil.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        return cfg;
    }
}

package com.rcm.engineering.resource.utils;

import com.itextpdf.html2pdf.HtmlConverter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.rcm.engineering.domain.Attendance;
import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.domain.Employee;
import com.rcm.engineering.repository.EmployeeRepository;
import freemarker.template.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@org.springframework.context.annotation.Configuration
public class FtlToPdfUtil {

    private final EmployeeRepository employeeRepository;

    public FtlToPdfUtil(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

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
                FtlToPdfUtil.class.getClassLoader(),
                "/templates"
        );
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Template template = cfg.getTemplate("challan.ftl");
        Map<String, Object> model = new HashMap<>();

        ClassPathResource resource = new ClassPathResource("static/images/logo.png");
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
        String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
        model.put("logoBase64", base64Logo);

        model.put("challan", challan);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
    public static void generatePayslip(Employee emp,
                                       List<Attendance> records,
                                       long presentDays,
                                       double totalSalary,
                                       OutputStream out) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("emp", emp);
            model.put("presentDays", presentDays);
            model.put("totalSalary", totalSalary);

            if (!records.isEmpty()) {
                LocalDate firstDate = records.get(0).getDate();
                String monthYear = firstDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                model.put("monthYear", monthYear);
            }

            DateTimeFormatter commonFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            model.put("dob", emp.getDateOfBirth().format(commonFormat));
            model.put("doj", emp.getDateOfJoining().format(commonFormat));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            List<Map<String, Object>> formattedRecords = records.stream().map(att -> {
                Map<String, Object> m = new HashMap<>();
                m.put("date", att.getDate() != null ? att.getDate().format(dateFormatter) : "—");
                m.put("status", att.getStatus().toString());
                m.put("checkIn", att.getCheckInDateTime() != null ? att.getCheckInDateTime().format(timeFormatter).toUpperCase() : "—");
                m.put("checkOut", att.getCheckOutDateTime() != null ? att.getCheckOutDateTime().format(timeFormatter).toUpperCase() : "—");

                String workingHours = "0 Hrs 0 Mins";
                if (att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null) {
                    Duration duration = Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    workingHours = String.format("%d Hrs %d Mins", hours, minutes);
                }
                m.put("totalHours", workingHours);
                return m;
            }).collect(Collectors.toList());
            model.put("records", formattedRecords);
            long totalMinutes = records.stream()
                    .filter(att -> att.getCheckInDateTime() != null && att.getCheckOutDateTime() != null)
                    .mapToLong(att -> Duration.between(att.getCheckInDateTime(), att.getCheckOutDateTime()).toMinutes())
                    .sum();
            long totalHours = totalMinutes / 60;
            long remainingMinutes = totalMinutes % 60;
            model.put("totalWorkedHours", String.format("%d Hrs %d Mins", totalHours, remainingMinutes));
            ClassPathResource resource = new ClassPathResource("static/images/logo.png");
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
            model.put("logoBase64", base64Logo);
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

    // Profile
    public byte[] generateEmployeeProfile(String empCode, Configuration freemarkerConfig) {
        Employee employee = employeeRepository.findByEmpCode(empCode)
                .orElseThrow(() -> new RuntimeException("Employee not found with empCode: " + empCode));
        try {

            DateTimeFormatter commonFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Map<String, Object> model = new HashMap<>();
            model.put("employee", employee);
            model.put("generatedOn", LocalDate.now());
            model.put("dob", employee.getDateOfBirth().format(commonFormat));
            model.put("doj", employee.getDateOfJoining().format(commonFormat));

            ClassPathResource resource = new ClassPathResource("static/images/logo.png");
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
            model.put("logoBase64", base64Logo);

            Template template = freemarkerConfig.getTemplate("profile.ftl");
            String html = processTemplate(template, model);
            return generatePdfFromHtml(html);
        } catch (IOException | TemplateException ex) {
            throw new RuntimeException("Error generating PDF from FTL", ex);
        }
    }

    private static String processTemplate(Template template, Map<String, Object> dataModel)
            throws IOException, TemplateException {
        try (StringWriter writer = new StringWriter()) {
            template.process(dataModel, writer);
            return writer.toString();
        }
    }

    private static byte[] generatePdfFromHtml(String html) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new IOException("PDF rendering failed", ex);
        }
    }
}

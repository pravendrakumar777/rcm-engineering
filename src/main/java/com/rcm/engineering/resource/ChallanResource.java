package com.rcm.engineering.resource;

import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.repository.ChallanRepository;
import com.rcm.engineering.resource.utils.ExportUtils;
import com.rcm.engineering.resource.utils.FtlToPdfUtil;
import com.rcm.engineering.service.ChallanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChallanResource {

    private static final Logger log = LoggerFactory.getLogger(ChallanResource.class);
    private final ChallanService challanService;
    private final ChallanRepository challanRepository;

    public ChallanResource(ChallanService challanService, ChallanRepository challanRepository) {
        this.challanService = challanService;
        this.challanRepository = challanRepository;
    }


    // 1. Save Challan to DB
    @PostMapping("/challan")
    public ResponseEntity<Challan> createChallan(@RequestBody Challan challan) {
        log.info("REST Request to createChallan: {}", challan);
        return ResponseEntity.ok(challanService.saveChallan(challan));
    }

    // 2. Save + Generate PDF
    @PostMapping("/challan/generate")
    public ResponseEntity<?> generateChallan(@RequestBody Challan challan) {
        String generatedChallanNo = generateChallanNo();
        challan.setChallanNo(generatedChallanNo);
        try {
            Challan saved = challanService.saveChallan(challan);
            byte[] pdfBytes = FtlToPdfUtil.generateChallanPDF(saved);

            return ResponseEntity.ok()
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=challan_" + saved.getId() + ".pdf")
                    .body(new ByteArrayResource(pdfBytes));
        } catch (Exception e) {
            log.error("PDF generation failed", e);
            return ResponseEntity.status(500).body("Failed to generate Challan PDF");
        }
    }

   /* download pdf challan */
    @GetMapping("/challan/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadChallan(@PathVariable Long id) {
        try {
            Challan challan = challanService.getChallanById(id);
            byte[] pdfBytes = FtlToPdfUtil.generateChallanPDF(challan);

            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM_yyyy"));
            String challnNo = challan.getChallanNo();
            String fileName = challnNo + "_" + currentMonth  + "_challan.pdf";

            return ResponseEntity.ok()
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName)
                    .body(new ByteArrayResource(pdfBytes));
        } catch (Exception e) {
            log.error("PDF download failed", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/challans")
    public ResponseEntity<List<Challan>> getAllChallans() {
        log.info("REST Request to get all Challans");
        return ResponseEntity.ok(challanService.getAll());
    }

    @PostMapping("/challan/calculate")
    public ResponseEntity<Map<String, Object>> calculateChallan(@RequestBody Challan challan) {
        double grandTotal = challan.getItems().stream()
                .mapToDouble(ChallanItem::getTotalAmount)
                .sum();
        Map<String, Object> response = new HashMap<>();
        response.put("grandTotal", grandTotal);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/challan/{id}")
    public ResponseEntity<Challan> getChallanById(@PathVariable Long id) {
        try {
            Challan challan = challanService.getChallanById(id);
            return ResponseEntity.ok(challan);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String generateChallanNo() {
        String financialYear = String.valueOf(Year.now().getValue());
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        String day = String.format("%02d", today.getDayOfMonth());
        String month = String.format("%02d", today.getMonthValue());
        String hour = String.format("%02d", now.getHour());
        String minute = String.format("%02d", now.getMinute());
        String seconds = String.format("%02d", now.getSecond());
        String dm = day + month;
        String hm = hour + minute;
        return "RCMCN" + financialYear + dm + hm + seconds;
    }
    // excel sheet
    @GetMapping("/challan/download/excel/{id}")
    public ResponseEntity<ByteArrayResource> downloadChallanExcel(@PathVariable Long id) {
        try {
            Challan challan = challanService.getChallanById(id);
            byte[] excelBytes = ExportUtils.generateChallanExcel(challan);

            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM_yyyy"));
            String challnNo = challan.getChallanNo();
            String fileName = challnNo + "_" + currentMonth + "_challan.xlsx";

            return ResponseEntity.ok()
                    .contentLength(excelBytes.length)
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(excelBytes));
        } catch (Exception e) {
            log.error("Excel download failed", e);
            return ResponseEntity.status(500).build();
        }
    }
    // csv export
    @GetMapping("/challan/download/csv/{id}")
    public ResponseEntity<ByteArrayResource> downloadChallanCSV(@PathVariable Long id) {
        try {
            Challan challan = challanService.getChallanById(id);
            byte[] csvBytes = ExportUtils.generateChallanCSV(challan);
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM_yyyy"));
            String challnNo = challan.getChallanNo();
            String fileName = challnNo + "_" + currentMonth + "_challan.csv";

            return ResponseEntity.ok()
                    .contentLength(csvBytes.length)
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(csvBytes));
        } catch (Exception e) {
            log.error("CSV download failed", e);
            return ResponseEntity.status(500).build();
        }
    }
}
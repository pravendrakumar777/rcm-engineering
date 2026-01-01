package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.repository.ChallanRepository;
import com.rcm.engineering.service.ChallanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class ChallanServiceImpl implements ChallanService {

    private static final Logger log = LoggerFactory.getLogger(ChallanServiceImpl.class);
    private final ChallanRepository challanRepository;

    public ChallanServiceImpl(ChallanRepository challanRepository) {
        this.challanRepository = challanRepository;
    }

    @Override
    public Challan saveChallan(Challan challan) {
        log.info("Service Request to saveChallan: {}", challan);

        String companyCode = "RCM";
        String outCh = "OUT";
        String financialYear = String.valueOf(Year.now().getValue());

        String refChNo;
        boolean exists;
        do {

            LocalDate today = LocalDate.now();
            LocalDateTime now = LocalDateTime.now();
            String day = String.format("%02d", today.getDayOfMonth());
            String month = String.format("%02d", today.getMonthValue());
            String hour = String.format("%02d", now.getHour());
            String minute = String.format("%02d", now.getMinute());

            String dm = day + month;
            String hm = hour + minute;

            refChNo = companyCode + "/" + outCh + "/" + financialYear + "/" + dm + "/" + hm;
            exists = challanRepository.existsByRefChNo(refChNo);
            challan.setRefChNo(refChNo);
        } while (exists);
        challan.setRefChNo(refChNo);

        if (challan.getItems() != null) {
            challan.getItems().forEach(item -> {
                item.setChallan(challan);

                int totalPieces = (int) (Double.parseDouble(item.getWeight()) * item.getPiecesPerKg());
                double totalAmount = totalPieces * item.getRatePerPiece();
                item.setTotalPieces(totalPieces);
                item.setTotalAmount(totalAmount);
                item.setProcess(item.getProcess());
                item.setHsnCode(item.getHsnCode());
                item.setUnit(item.getUnit());
            });
        }
        return challanRepository.save(challan);
    }

    @Override
    public Optional<Challan> getChallan(Long id) {
        log.info("Service Request to getChallan: {}", id);
        return challanRepository.findById(id);
    }

    @Override
    public List<Challan> getAll() {
        List<Challan> result = challanRepository.findAll();
        result.sort((c1, c2) -> c2.getDate().compareTo(c1.getDate()));
        log.info("Service Request to getAll: {}");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
        for (Challan challan : result) {
            String formattedDate = challan.getDate() != null ? challan.getDate().format(formatter) : "NA";
            challan.setFormattedDate(formattedDate);
            boolean hasModifiedItems = challan.getItems() != null && challan.getItems().stream().anyMatch(ChallanItem::isAddedOnModifiedDate);
            String modifiedFormatted = (hasModifiedItems && challan.getModifiedDate() != null) ? challan.getModifiedDate().format(formatter) : "NA";
            challan.setModifiedFormatted(modifiedFormatted);
        }
        return result;
    }

    @Override
    public Challan getChallanById(Long id) {
        log.info("Service Request to getChallanById: {}", id);
        Challan result = challanRepository.findById(id).get();
        return result;
    }

    @Override
    @Transactional
    public Challan upsertItemInChallan(Long challanId, ChallanItem item) {
        log.info("Service Request to upsertItemInChallan: {}", challanId);
        Challan challan = getChallanById(challanId);
        if (item.getId() == null) {
            item.setAddedAt(LocalDateTime.now());
            item.setAddedOnModifiedDate(true);
            item.setChallan(challan);
            challan.getItems().add(item);
        } else {
            // item update
            ChallanItem existing = challan.getItems().stream()
                    .filter(ci -> ci.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            existing.setDescription(item.getDescription());
            existing.setWeight(item.getWeight());
            existing.setPiecesPerKg(item.getPiecesPerKg());
            existing.setRatePerPiece(item.getRatePerPiece());
            existing.setTotalPieces(item.getTotalPieces());
            existing.setTotalAmount(item.getTotalAmount());
            existing.setProcess(item.getProcess());
            existing.setHsnCode(item.getHsnCode());
            existing.setUnit(item.getUnit());
        }
        challan.setModifiedDate(LocalDateTime.now());
        return challanRepository.save(challan);
    }
}

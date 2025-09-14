package com.rcm.engineering.service.impl;

import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;
import com.rcm.engineering.repository.ChallanRepository;
import com.rcm.engineering.service.ChallanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        if (challan.getItems() != null) {
            challan.getItems().forEach(item -> {
                item.setChallan(challan);

                int totalPieces = (int) (Double.parseDouble(item.getWeight()) * item.getPiecesPerKg());
                double totalAmount = totalPieces * item.getRatePerPiece();
                item.setTotalPieces(totalPieces);
                item.setTotalAmount(totalAmount);
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
        return result;
    }

    @Override
    public Challan getChallanById(Long id) {
        Challan result = challanRepository.findById(id).get();
        return result;
    }
}

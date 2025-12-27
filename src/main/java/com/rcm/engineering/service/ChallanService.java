package com.rcm.engineering.service;

import com.rcm.engineering.domain.Challan;
import com.rcm.engineering.domain.ChallanItem;

import java.util.List;
import java.util.Optional;
public interface ChallanService {
    Challan saveChallan(Challan challan);
    Optional<Challan> getChallan(Long id);
    List<Challan> getAll();
    Challan getChallanById(Long id);
    Challan upsertItemInChallan(Long challanId, ChallanItem item);
}

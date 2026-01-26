package com.niiran.software.solutions.service;

import com.niiran.software.solutions.domain.Challan;
import com.niiran.software.solutions.domain.ChallanItem;

import java.util.List;
import java.util.Optional;
public interface ChallanService {
    Challan saveChallan(Challan challan);
    Optional<Challan> getChallan(Long id);
    List<Challan> getAll();
    Challan getChallanById(Long id);
    Challan upsertItemInChallan(Long challanId, ChallanItem item);
}

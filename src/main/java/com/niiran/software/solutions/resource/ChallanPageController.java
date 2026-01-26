package com.niiran.software.solutions.resource;

import com.niiran.software.solutions.domain.Challan;
import com.niiran.software.solutions.repository.ChallanRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChallanPageController {
    private final ChallanRepository challanRepository;

    public ChallanPageController(ChallanRepository challanRepository) {
        this.challanRepository = challanRepository;
    }

    @GetMapping("/challans")
    public String viewChallanListPage() {
        return "challan-list";
    }

    @GetMapping("/challan/form")
    public String showChallanFormPage() {
        return "challan-form";
    }

    @GetMapping("/challan/edit/{id}")
    public String showEditChallanPage(@PathVariable Long id, Model model) {
        Challan challan = challanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Challan not found"));
        model.addAttribute("challan", challan);
        return "challan-form";
    }
}

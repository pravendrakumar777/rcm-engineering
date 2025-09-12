package com.rcm.engineering.resource;

import com.rcm.engineering.service.ChallanService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ChallanPageController {
    private final ChallanService challanService;
    public ChallanPageController(ChallanService challanService) {
        this.challanService = challanService;
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/challans";
    }

    @GetMapping("/challans")
    public String viewChallanListPage() {
        return "challan-list";
    }

    @GetMapping("/challan/form")
    public String showChallanFormPage() {
        return "challan-form";
    }
}

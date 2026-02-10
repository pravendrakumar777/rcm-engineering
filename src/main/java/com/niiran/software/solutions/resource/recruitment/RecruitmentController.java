package com.niiran.software.solutions.resource.recruitment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecruitmentController {
    @GetMapping("/recruitment-interviews")
    public String page(){
        return "recruitment";
    }
}

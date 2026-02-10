package com.niiran.software.solutions.resource.leave;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leaves-policies")
public class LeaveViewController {
    @GetMapping
    public String page() {
        return "leave_management";
    }
}

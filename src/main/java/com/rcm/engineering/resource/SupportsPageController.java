package com.rcm.engineering.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supports")
public class SupportsPageController {

    @GetMapping
    public String supportsPage() {
        return "supports";
    }
}

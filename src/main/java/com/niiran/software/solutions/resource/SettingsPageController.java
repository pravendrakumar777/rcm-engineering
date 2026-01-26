package com.niiran.software.solutions.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsPageController {

    @GetMapping
    public String settingsPage() {
        return "settings";
    }
}

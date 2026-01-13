package com.rcm.engineering.resource;

import com.rcm.engineering.domain.User;
import com.rcm.engineering.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //@GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("availableRoles", Arrays.asList("USER", "ADMIN", "SUPER_ADMIN"));
        return "register";
    }

    //@PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam("roles") List<String> roles) {
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
        return "redirect:/login";
    }

    //@GetMapping("/login")
    public String login() {
        return "login";
    }

}

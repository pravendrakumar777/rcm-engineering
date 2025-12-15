package com.rcm.engineering.resource.rest;

import com.rcm.engineering.constants.ApplicationConstants;
import com.rcm.engineering.domain.User;
import com.rcm.engineering.repository.UserRepository;
import com.rcm.engineering.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthRestController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (user.getUsername() == null || user.getUsername().trim().length() < 3) {
                response.put("message", "Username must be at least 3 characters.");
                return ResponseEntity.badRequest().body(response);
            }

            if (user.getPassword() == null || user.getPassword().trim().length() < 4) {
                response.put("message", "Password must be at least 4 characters.");
                return ResponseEntity.badRequest().body(response);
            }

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                response.put("message", "Please select at least one role.");
                return ResponseEntity.badRequest().body(response);
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                response.put("message", "Username already exists.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            userService.save(user);
            response.put("message", ApplicationConstants.USER_REGISTERED_SUCCESSFULLY);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while registering user.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return ResponseEntity.ok(
                    Collections.singletonMap("message", ApplicationConstants.LOGIN_SUCCESS)
            );
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApplicationConstants.INVALID_CREDENTIALS);
    }
}

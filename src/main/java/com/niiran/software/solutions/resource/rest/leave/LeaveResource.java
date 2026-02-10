package com.niiran.software.solutions.resource.rest.leave;

import com.niiran.software.solutions.domain.leave.LeaveRequest;
import com.niiran.software.solutions.service.leave.LeaveService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin
public class LeaveResource {
    private final LeaveService leaveService;
    public LeaveResource(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public LeaveRequest apply(@RequestBody LeaveRequest request) {
        return leaveService.applyLeave(request);
    }

    @GetMapping("/list")
    public List<LeaveRequest> list() {
        return leaveService.getAll();
    }

    @PutMapping("/approve/{id}")
    public LeaveRequest approve(@PathVariable Long id) {
        return leaveService.approve(id);
    }

    @PutMapping("/reject/{id}")
    public LeaveRequest reject(@PathVariable Long id) {
        return leaveService.reject(id);
    }
}

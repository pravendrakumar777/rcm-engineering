package com.niiran.software.solutions.service.leave;

import com.niiran.software.solutions.domain.leave.LeaveRequest;

import java.util.List;

public interface LeaveService {
    LeaveRequest applyLeave(LeaveRequest request);
    List<LeaveRequest> getAll();
    LeaveRequest approve(Long id);
    LeaveRequest reject(Long id);
}

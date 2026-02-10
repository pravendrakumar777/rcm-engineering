package com.niiran.software.solutions.service.impl.leave;

import com.niiran.software.solutions.domain.leave.LeaveRequest;
import com.niiran.software.solutions.exceptions.ResourceNotFoundException;
import com.niiran.software.solutions.repository.leave.LeaveRepository;
import com.niiran.software.solutions.service.leave.LeaveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public LeaveRequest applyLeave(LeaveRequest request) {
        return leaveRepository.save(request);
    }

    @Override
    public List<LeaveRequest> getAll() {
        return leaveRepository.findAllByOrderByAppliedDateDesc();
    }

    @Override
    public LeaveRequest approve(Long id) {
        LeaveRequest request = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Request not found with id: " + id));
        request.setStatus("APPROVED");
        return leaveRepository.save(request);
    }

    @Override
    public LeaveRequest reject(Long id) {
        LeaveRequest request = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Request not found with id: " + id));
        request.setStatus("REJECTED");
        return leaveRepository.save(request);
    }
}

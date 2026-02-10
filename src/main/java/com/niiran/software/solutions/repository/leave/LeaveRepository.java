package com.niiran.software.solutions.repository.leave;

import com.niiran.software.solutions.domain.leave.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findAllByOrderByAppliedDateDesc();

    List<LeaveRequest> findByOhrIdOrderByAppliedDateDesc(String ohrId);
}

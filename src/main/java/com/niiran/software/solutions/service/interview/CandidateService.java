package com.niiran.software.solutions.service.interview;

import com.niiran.software.solutions.domain.interview.Candidate;

import java.util.List;

public interface CandidateService {

    Candidate save(Candidate c);
    List<Candidate> getAll();
    Candidate updateStatus(Long id, String status);
}

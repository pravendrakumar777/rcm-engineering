package com.niiran.software.solutions.service.impl.interview;

import com.niiran.software.solutions.domain.interview.Candidate;
import com.niiran.software.solutions.exceptions.ResourceNotFoundException;
import com.niiran.software.solutions.repository.interview.CandidateRepository;
import com.niiran.software.solutions.service.interview.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public List<Candidate> getAll() {
        return candidateRepository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public Candidate updateStatus(Long id, String status) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        candidate.setStatus(status);
        return candidateRepository.save(candidate);
    }
}

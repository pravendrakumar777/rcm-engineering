package com.niiran.software.solutions.repository.interview;

import com.niiran.software.solutions.domain.interview.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findAllByOrderByCreatedDateDesc();
}

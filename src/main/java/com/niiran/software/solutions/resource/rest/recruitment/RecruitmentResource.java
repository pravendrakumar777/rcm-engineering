package com.niiran.software.solutions.resource.rest.recruitment;

import com.niiran.software.solutions.domain.interview.Candidate;
import com.niiran.software.solutions.service.interview.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruitment")
@CrossOrigin
public class RecruitmentResource {
    private final CandidateService candidateService;
    public RecruitmentResource(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/add")
    public Candidate add(@RequestBody Candidate c){
        return candidateService.save(c);
    }

    @GetMapping("/list")
    public List<Candidate> list(){
        return candidateService.getAll();
    }

    @PutMapping("/status/{id}/{status}")
    public Candidate status(@PathVariable Long id,
                            @PathVariable String status){
        return candidateService.updateStatus(id, status);
    }
}

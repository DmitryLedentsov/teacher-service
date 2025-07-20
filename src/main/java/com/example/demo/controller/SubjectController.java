package com.example.demo.controller;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.service.SubjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@SecurityRequirement(name = "bearer-token")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDto> create(
            Principal principal,
            @RequestBody @Valid SubjectCreationDto subjectCreation
    ) {
        var subject = subjectService.create(principal.getName(), subjectCreation);
        var uri = createSubjectURI(subject.id());
        return ResponseEntity.created(uri).body(subject);
    }

    @GetMapping
    public List<SubjectDto> getAll(Principal principal) {
        return subjectService.getAllByUsername(principal.getName());
    }

    @GetMapping("/{id}")
    public SubjectDto get(Principal principal, @PathVariable Long id) {
        return subjectService.getByUsernameAndId(principal.getName(), id);
    }

    private URI createSubjectURI(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.SubjectCreationDto;
import com.example.demo.dto.SubjectDto;
import com.example.demo.service.SubjectService;
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
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDto> create(Principal principal, @RequestBody @Valid SubjectCreationDto subjectCreationDto) {
        var subjectDto = subjectService.create(principal.getName(), subjectCreationDto);
        var uri = createURI(subjectDto.id());
        return ResponseEntity.created(uri).body(subjectDto);
    }

    @GetMapping
    public List<SubjectDto> getAll(Principal principal) {
        return subjectService.getAll(principal.getName());
    }

    @GetMapping("/{id}")
    public SubjectDto get(Principal principal, @PathVariable Long id) {
        return subjectService.getById(principal.getName(), id);
    }

    private URI createURI(Number id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .build(id);
    }
}

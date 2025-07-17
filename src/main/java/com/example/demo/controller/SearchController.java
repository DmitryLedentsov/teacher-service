package com.example.demo.controller;

import com.example.demo.dto.external.SearchResponseDto;
import com.example.demo.service.ExternalApiService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final ExternalApiService externalApiService;
    private final UserService userService;

    // TODO переделать под POST?
    @GetMapping
    public SearchResponseDto search(Principal principal, @RequestParam String subject, @RequestParam String query) {
        var userId = userService.findByUsername(principal.getName()).getId();
        return externalApiService.search(userId.toString(), subject, query);
    }
}

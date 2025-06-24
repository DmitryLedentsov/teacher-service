package com.example.demo.controller;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public TokenDto signUp(@RequestBody @Valid AuthDto authDto) {
        return authService.signUp(authDto);
    }

    @PostMapping("/sign-in")
    public TokenDto signIn(@RequestBody AuthDto authDto) {
        return authService.signIn(authDto);
    }
}

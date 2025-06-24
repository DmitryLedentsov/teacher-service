package com.example.demo.service.impl;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public TokenDto signUp(AuthDto authDto) {
        var userDetails = userService.createPrincipal(authDto);
        var token = jwtProvider.generateToken(userDetails);
        return new TokenDto(token);
    }

    @Override
    public TokenDto signIn(AuthDto authDto) {
        // TODO здесь может выброситься BadCredentialsException
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.username(), authDto.password())
        );

        var userDetails = (UserPrincipal) authentication.getPrincipal();
        var token = jwtProvider.generateToken(userDetails);
        return new TokenDto(token);
    }
}

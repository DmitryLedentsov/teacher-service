package com.example.demo.service;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.TokenDto;

public interface AuthService {
    TokenDto signUp(AuthDto authDto);

    TokenDto signIn(AuthDto authDto);
}

package com.example.demo.service;

import com.example.demo.dto.AuthDto;
import com.example.demo.entity.User;
import com.example.demo.security.UserPrincipal;

public interface UserService {
    UserPrincipal createPrincipal(AuthDto authDto);

    User getByUsername(String username);
}

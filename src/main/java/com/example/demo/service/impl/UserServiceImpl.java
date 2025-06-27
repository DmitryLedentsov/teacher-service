package com.example.demo.service.impl;

import com.example.demo.dto.AuthDto;
import com.example.demo.entity.User;
import com.example.demo.exception.EntityAlreadyExistsException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.repo.UserRepository;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.UserService;
import com.example.demo.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserPrincipal createPrincipal(AuthDto authDto) {
        validate(authDto);

        // TODO потом добавить MapStruct
        var user = new User();
        user.setUsername(authDto.username());
        user.setPassword(passwordEncoder.encode(authDto.password()));
        user.setHash(authDto.username().hashCode());

        userRepository.save(user);

        return new UserPrincipal(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным именем не существует"));
    }

    private void validate(AuthDto authDto) {
        if (userRepository.existsByUsername(authDto.username()))
            throw new EntityAlreadyExistsException(ValidationUtils.USERNAME_TAKEN);
    }
}

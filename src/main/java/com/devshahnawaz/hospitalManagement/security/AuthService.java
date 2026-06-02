package com.devshahnawaz.hospitalManagement.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.devshahnawaz.hospitalManagement.dto.LoginRequestDto;
import com.devshahnawaz.hospitalManagement.dto.LoginResponseDto;
import com.devshahnawaz.hospitalManagement.dto.SignupResponseDto;
import com.devshahnawaz.hospitalManagement.entity.User;
import com.devshahnawaz.hospitalManagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getId());
    }

    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if (user != null)
            throw new IllegalArgumentException("User Already Exists");

        user = userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .password(signupRequestDto.getPassword())
                .build());

        return new SignupResponseDto(user.getId(), user.getUsername());

    }
}

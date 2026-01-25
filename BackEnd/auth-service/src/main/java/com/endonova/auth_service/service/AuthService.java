package com.endonova.auth_service.service;

import com.endonova.auth_service.dto.AuthResponse;
import com.endonova.auth_service.model.User;
import com.endonova.auth_service.repository.UserRepository;
import com.endonova.auth_service.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole("USER");
        return userRepository.save(u);
    }

    public AuthResponse login(String username, String password) {
        User u = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        String access = jwtUtil.generateAccessToken(u);
        String refresh = refreshTokenService.createRefreshToken(u).getToken();
        return new AuthResponse(access, refresh);
    }

    public AuthResponse refresh(String refreshToken) {
        var opt = refreshTokenService.findByToken(refreshToken);
        if (opt.isEmpty()) throw new RuntimeException("Refresh token inválido");
        var token = opt.get();
        if (refreshTokenService.isExpired(token)) throw new RuntimeException("Refresh token expirado");
        User u = token.getUser();
        String access = jwtUtil.generateAccessToken(u);
        return new AuthResponse(access, refreshToken);
    }
}

package com.endonova.auth_service.controller;

import com.endonova.auth_service.dto.AuthRequest;
import com.endonova.auth_service.dto.AuthResponse;
import com.endonova.auth_service.model.User;
import com.endonova.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest req) {
        User u = authService.register(req.getUsername(), req.getPassword());
        u.setPassword(null);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse resp = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        // body expect raw token string or JSON string; accept both
        String token = refreshToken.replaceAll("\"", "");
        AuthResponse r = authService.refresh(token);
        return ResponseEntity.ok(r);
    }
}

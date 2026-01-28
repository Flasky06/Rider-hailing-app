package com.tritva.percel_delivery.controller;

import com.tritva.percel_delivery.model.dto.AuthDTOs;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody AuthDTOs.UserRegistrationDTO dto) {
        User user = authService.registerClient(dto);
        return ResponseEntity.ok("User registered successfully via ID: " + user.getId());
    }
}

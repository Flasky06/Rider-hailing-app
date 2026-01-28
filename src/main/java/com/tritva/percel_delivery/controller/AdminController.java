package com.tritva.percel_delivery.controller;

import com.tritva.percel_delivery.model.dto.AuthDTOs;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

    @PostMapping("/riders")
    @PreAuthorize("hasRole('ADMIN')") // Just in case, though SecurityConfig handles it too
    public ResponseEntity<String> createRider(@RequestBody AuthDTOs.RiderCreationDTO dto) {
        Rider rider = authService.createRider(dto);
        return ResponseEntity.ok("Rider created successfully with ID: " + rider.getId());
    }
}

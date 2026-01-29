package com.tritva.percel_delivery.controller;

import com.tritva.percel_delivery.model.dto.DeliveryRequestDTO;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.repository.UserRepository;
import com.tritva.percel_delivery.services.DeliveryLifecycleService;
import com.tritva.percel_delivery.services.RiderAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final RiderAssignmentService riderAssignmentService;
    private final DeliveryLifecycleService lifecycleService;
    private final UserRepository userRepository;

    @GetMapping("/history")
    public ResponseEntity<List<DeliveryResponseDTO>> getMyHistory(Authentication authentication) {
        String email = authentication.getName();
        com.tritva.percel_delivery.model.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(lifecycleService.getClientHistory(user));
    }
}

package com.tritva.percel_delivery.controller;

import com.tritva.percel_delivery.model.DeliveryStatus;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.UserRepository;
import com.tritva.percel_delivery.services.impl.DeliveryLifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rider")
@RequiredArgsConstructor
public class RiderOperationsController {

    private final DeliveryLifecycleService lifecycleService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/deliveries")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<List<DeliveryResponseDTO>> getMyDeliveries(Authentication authentication) {
        User rider = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(lifecycleService.getRiderDeliveries(rider));
    }

    @PatchMapping("/deliveries/{id}/status")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<DeliveryResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam DeliveryStatus status,
            Authentication authentication) {
        User rider = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(lifecycleService.updateStatus(id, status, rider));
    }

    @PatchMapping("/location")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<String> updateLocation(
            @RequestParam Double lat,
            @RequestParam Double lon,
            Authentication authentication) {
        User riderUser = getAuthenticatedUser(authentication);
        Rider rider = riderUser.getRiderProfile();
        if (rider == null) {
            throw new RuntimeException("User is not a rider");
        }

        lifecycleService.updateRiderLocation(rider, lat, lon);

        return ResponseEntity.ok("Location updated");
    }
}

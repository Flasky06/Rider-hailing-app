package com.tritva.percel_delivery.services.impl;

import com.tritva.percel_delivery.mapper.DeliveryMapper;
import com.tritva.percel_delivery.model.DeliveryStatus;
import com.tritva.percel_delivery.model.dto.DeliveryRequestDTO;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.model.entity.DeliveryRequest;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.DeliveryRequestRepository;
import com.tritva.percel_delivery.repository.RiderRepository;
import com.tritva.percel_delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.tritva.percel_delivery.services.NotificationService;
import com.tritva.percel_delivery.services.RiderAssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderAssignmentServiceImpl implements RiderAssignmentService {

    private final RiderRepository riderRepository;
    private final DeliveryRequestRepository deliveryRequestRepository;
    private final DeliveryMapper deliveryMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public DeliveryResponseDTO requestDelivery(DeliveryRequestDTO requestDTO) {
        User client = userRepository.findById(requestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (requestDTO.getPickupLat() == null || requestDTO.getPickupLon() == null) {
            throw new RuntimeException("Pickup coordinates are required for assignment");
        }

        // 1. Find ALL available riders
        List<Rider> availableRiders = riderRepository.findAvailableRidersSortedByLoad();

        if (availableRiders.isEmpty()) {
            throw new RuntimeException("No riders available");
        }

        // 2. Find Closest Rider using Haversine
        Rider bestRider = null;
        double minDistance = Double.MAX_VALUE;

        for (Rider rider : availableRiders) {
            if (rider.getCurrentLat() == null || rider.getCurrentLon() == null) {
                continue; // Skip riders without location
            }
            double distance = calculateDistance(
                    requestDTO.getPickupLat(), requestDTO.getPickupLon(),
                    rider.getCurrentLat(), rider.getCurrentLon());
            if (distance < minDistance) {
                minDistance = distance;
                bestRider = rider;
            }
        }

        if (bestRider == null) {
            // Fallback: Just pick the first available one if no locations are set
            bestRider = availableRiders.get(0);
        }

        // 3. Assign
        bestRider.setAvailable(false);
        bestRider.setDeliveriesToday(bestRider.getDeliveriesToday() + 1);
        riderRepository.save(bestRider);

        DeliveryRequest request = deliveryMapper.toEntity(requestDTO);
        request.setClient(client);
        request.setAssignedRider(bestRider);
        request.setStatus(DeliveryStatus.ASSIGNED);

        DeliveryRequest savedRequest = deliveryRequestRepository.save(request);

        // Send Notification
        notificationService.sendAssignmentEmail(client.getEmail(), bestRider.getUser().getName());

        return deliveryMapper.toResponseDTO(savedRequest);
    }

    // Haversine Formula (Returns distance in km)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

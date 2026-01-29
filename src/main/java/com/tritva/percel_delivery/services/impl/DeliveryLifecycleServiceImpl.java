package com.tritva.percel_delivery.services.impl;

import com.tritva.percel_delivery.mapper.DeliveryMapper;
import com.tritva.percel_delivery.model.DeliveryStatus;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.model.entity.DeliveryRequest;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.DeliveryRequestRepository;
import com.tritva.percel_delivery.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import com.tritva.percel_delivery.services.DeliveryLifecycleService;
import com.tritva.percel_delivery.services.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryLifecycleServiceImpl implements DeliveryLifecycleService {

    private final DeliveryRequestRepository deliveryRepository;
    private final RiderRepository riderRepository;
    private final DeliveryMapper deliveryMapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getClientHistory(User client) {
        return deliveryRepository.findByClient_Id(client.getId()).stream()
                .map(deliveryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeliveryResponseDTO> getRiderDeliveries(User riderUser) {
        Rider rider = riderUser.getRiderProfile();
        if (rider == null) {
            throw new RuntimeException("User is not a rider profile");
        }
        return deliveryRepository.findByAssignedRider_Id(rider.getId()).stream()
                .map(deliveryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeliveryResponseDTO updateStatus(UUID deliveryId, DeliveryStatus newStatus, User riderUser) {
        Rider rider = riderUser.getRiderProfile();
        if (rider == null) {
            throw new RuntimeException("User is not a rider");
        }

        DeliveryRequest delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        if (!delivery.getAssignedRider().getId().equals(rider.getId())) {
            throw new RuntimeException("You are not assigned to this delivery");
        }

        delivery.setStatus(newStatus);

        // Logic: specific transitions or side-effects
        if (newStatus == DeliveryStatus.DELIVERED) {
            // Free up the rider
            rider.setAvailable(true);
            riderRepository.save(rider);

            // Send Notification
            notificationService.sendDeliveryEmail(delivery.getClient().getEmail(), delivery.getId().toString());
        }

        DeliveryRequest saved = deliveryRepository.save(delivery);
        return deliveryMapper.toResponseDTO(saved);
    }

    @Transactional
    public void updateRiderLocation(Rider rider, Double lat, Double lon) {
        rider.setCurrentLat(lat);
        rider.setCurrentLon(lon);
        riderRepository.save(rider);
    }
}

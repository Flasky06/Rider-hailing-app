package com.tritva.percel_delivery.services;

import com.tritva.percel_delivery.model.DeliveryStatus;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface DeliveryLifecycleService {
    List<DeliveryResponseDTO> getClientHistory(User client);

    List<DeliveryResponseDTO> getRiderDeliveries(User riderUser);

    DeliveryResponseDTO updateStatus(UUID deliveryId, DeliveryStatus newStatus, User riderUser);

    void updateRiderLocation(Rider rider, Double lat, Double lon);
}

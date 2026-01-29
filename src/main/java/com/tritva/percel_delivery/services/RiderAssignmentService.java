package com.tritva.percel_delivery.services;

import com.tritva.percel_delivery.model.dto.DeliveryRequestDTO;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;

public interface RiderAssignmentService {
    DeliveryResponseDTO requestDelivery(DeliveryRequestDTO requestDTO);
}

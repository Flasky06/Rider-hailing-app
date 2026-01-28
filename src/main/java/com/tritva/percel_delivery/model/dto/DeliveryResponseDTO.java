package com.tritva.percel_delivery.model.dto;

import com.tritva.percel_delivery.model.DeliveryStatus;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class DeliveryResponseDTO {
    private UUID requestId;
    private String pickupLocation;
    private String dropoffLocation;
    private DeliveryStatus status;
    private String assignedRiderName;
    private UUID assignedRiderId;
}

package com.tritva.percel_delivery.model.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DeliveryRequestDTO {
    private UUID clientId;
    private String pickupLocation;
    private Double pickupLat;
    private Double pickupLon;
    private String dropoffLocation;
    private Double dropoffLat;
    private Double dropoffLon;
}

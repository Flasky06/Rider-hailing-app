package com.tritva.percel_delivery.model.entity;

import com.tritva.percel_delivery.model.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "delivery_requests")
public class DeliveryRequest extends BaseEntity {

    @Column(nullable = false)
    private String pickupLocation;

    @Column
    private Double pickupLat;

    @Column
    private Double pickupLon;

    @Column(nullable = false)
    private String dropoffLocation;

    @Column
    private Double dropoffLat;

    @Column
    private Double dropoffLon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider assignedRider;
}

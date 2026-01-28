package com.tritva.percel_delivery.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "riders")
public class Rider extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private boolean available = true;

    @Column(nullable = false)
    private int deliveriesToday = 0;

    @Column(nullable = false)
    private boolean active = true;

    private Double currentLat;
    private Double currentLon;
}

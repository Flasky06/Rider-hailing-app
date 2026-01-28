package com.tritva.percel_delivery.repository;

import com.tritva.percel_delivery.model.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RiderRepository extends JpaRepository<Rider, UUID> {

    // Find active riders who are available, sorted by deliveries today (ASC)
    // This implements the "Load Balancing" logic directly in the query
    @Query("SELECT r FROM Rider r WHERE r.active = true AND r.available = true ORDER BY r.deliveriesToday ASC")
    List<Rider> findAvailableRidersSortedByLoad();
}

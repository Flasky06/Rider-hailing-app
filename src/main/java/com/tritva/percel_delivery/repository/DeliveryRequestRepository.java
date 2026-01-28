package com.tritva.percel_delivery.repository;

import com.tritva.percel_delivery.model.entity.DeliveryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, UUID> {
    List<DeliveryRequest> findByClient_Id(UUID clientId);

    List<DeliveryRequest> findByAssignedRider_Id(UUID riderId);
}

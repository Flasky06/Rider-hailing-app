package com.tritva.percel_delivery.mapper;

import com.tritva.percel_delivery.model.dto.DeliveryRequestDTO;
import com.tritva.percel_delivery.model.dto.DeliveryResponseDTO;
import com.tritva.percel_delivery.model.entity.DeliveryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "client", ignore = true) // Set manually in service
    @Mapping(target = "assignedRider", ignore = true) // Set manually in service
    @Mapping(target = "status", ignore = true) // Set manually
    DeliveryRequest toEntity(DeliveryRequestDTO dto);

    @Mapping(target = "requestId", source = "id")
    @Mapping(target = "assignedRiderName", source = "assignedRider.user.name")
    @Mapping(target = "assignedRiderId", source = "assignedRider.id")
    DeliveryResponseDTO toResponseDTO(DeliveryRequest entity);
}

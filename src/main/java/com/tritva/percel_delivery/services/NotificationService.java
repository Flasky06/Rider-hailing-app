package com.tritva.percel_delivery.services;

public interface NotificationService {
    void sendAssignmentEmail(String toEmail, String riderName);

    void sendDeliveryEmail(String toEmail, String deliveryId);
}

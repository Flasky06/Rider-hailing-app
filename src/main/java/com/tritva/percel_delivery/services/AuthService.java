package com.tritva.percel_delivery.services;

import com.tritva.percel_delivery.model.dto.AuthDTOs;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;

public interface AuthService {
    User registerClient(AuthDTOs.UserRegistrationDTO dto);

    Rider createRider(AuthDTOs.RiderCreationDTO dto);

    void verifyEmail(String token);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}

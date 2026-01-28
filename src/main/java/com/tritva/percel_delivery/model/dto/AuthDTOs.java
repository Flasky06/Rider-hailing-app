package com.tritva.percel_delivery.model.dto;

import lombok.Data;

public class AuthDTOs {

    @Data
    public static class UserRegistrationDTO {
        private String name;
        private String email;
        private String password;
    }

    @Data
    public static class RiderCreationDTO {
        private String name;
        private String email;
        private String password;
        private boolean initialAvailability = true;
    }
}

package com.tritva.percel_delivery.services.impl;

import com.tritva.percel_delivery.model.Role;
import com.tritva.percel_delivery.model.dto.AuthDTOs;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.RiderRepository;
import com.tritva.percel_delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerClient(AuthDTOs.UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.CLIENT)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public Rider createRider(AuthDTOs.RiderCreationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.RIDER)
                .active(true)
                .build();

        userRepository.save(user);

        Rider rider = Rider.builder()
                .user(user)
                .available(dto.isInitialAvailability())
                .deliveriesToday(0)
                .build();

        return riderRepository.save(rider);
    }
}

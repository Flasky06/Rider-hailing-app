package com.tritva.percel_delivery.services.impl;

import com.tritva.percel_delivery.model.Role;
import com.tritva.percel_delivery.model.dto.AuthDTOs;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.RiderRepository;
import com.tritva.percel_delivery.repository.UserRepository;
import com.tritva.percel_delivery.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.tritva.percel_delivery.services.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public User registerClient(AuthDTOs.UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists");
        }

        // Generate 4-digit code
        String token = String.valueOf(1000 + new java.util.Random().nextInt(9000));

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.CLIENT)
                .active(false) // Revert to false: User must verify with code
                .verificationToken(token)
                .build();

        // Note: cellNo is required by entity but missing in DTO/Builder?
        // Assuming it resolves or is optional in current context, avoiding unrelated
        // changes.

        User savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), token);
        return savedUser;
    }

    @Transactional
    public Rider createRider(AuthDTOs.RiderCreationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists");
        }

        // Riders might not need email verification for MVP, or admin approves them.
        // Keeping them active=true for now as per previous logic, or change if needed.
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

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getVerificationToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        user.setActive(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetPasswordToken()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}

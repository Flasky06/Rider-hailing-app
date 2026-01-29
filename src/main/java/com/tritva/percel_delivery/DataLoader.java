package com.tritva.percel_delivery;

import com.tritva.percel_delivery.model.Role;
import com.tritva.percel_delivery.model.entity.Rider;
import com.tritva.percel_delivery.model.entity.User;
import com.tritva.percel_delivery.repository.RiderRepository;
import com.tritva.percel_delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RiderRepository riderRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode("password");

            // Create Admin
            User admin = User.builder()
                    .name("Super Admin")
                    .email("admin@demo.com")
                    .cellNo("0000000000") // Dummy
                    .password(encodedPassword)
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Created Admin with ID: " + admin.getId());
            System.out.println("Admin Creds: admin@demo.com / password");

            // Create Client
            User client = User.builder()
                    .name("Demo Client")
                    .email("client@demo.com")
                    .cellNo("1111111111")
                    .password(encodedPassword)
                    .role(Role.CLIENT)
                    .active(true)
                    .build();
            userRepository.save(client);
            System.out.println("Created Demo Client with ID: " + client.getId());
            System.out.println("Credentials: client@demo.com / password");

            // Create Rider 1 (Busy)
            User userRider1 = User.builder()
                    .name("Rider John (Busy)")
                    .email("john@demo.com")
                    .cellNo("2222222222")
                    .password(encodedPassword)
                    .role(Role.RIDER)
                    .active(true).build();
            userRepository.save(userRider1);
            Rider rider1 = Rider.builder().user(userRider1).available(false).deliveriesToday(5).build();
            riderRepository.save(rider1);

            // Create Rider 2 (Available, High Load)
            User userRider2 = User.builder()
                    .name("Rider Jane (Avail, High Load)")
                    .email("jane@demo.com")
                    .cellNo("3333333333")
                    .password(encodedPassword)
                    .role(Role.RIDER)
                    .active(true).build();
            userRepository.save(userRider2);
            Rider rider2 = Rider.builder().user(userRider2).available(true).deliveriesToday(10).build();
            riderRepository.save(rider2);

            // Create Rider 3 (Available, Low Load) - SHOULD BE PICKED
            User userRider3 = User.builder()
                    .name("Rider Doe (Avail, Low Load)")
                    .email("doe@demo.com")
                    .cellNo("4444444444")
                    .password(encodedPassword)
                    .role(Role.RIDER)
                    .active(true).build();
            userRepository.save(userRider3);
            Rider rider3 = Rider.builder().user(userRider3).available(true).deliveriesToday(2).build();
            riderRepository.save(rider3);

            System.out.println("Seeded Demo Riders. Creds: (john|jane|doe)@demo.com / password");
        }
    }

    @SpringBootApplication
    public static class PercelDeliveryApplication {

        public static void main(String[] args) {
            SpringApplication.run(PercelDeliveryApplication.class, args);
        }

    }
}
